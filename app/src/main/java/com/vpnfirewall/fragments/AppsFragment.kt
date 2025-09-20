package com.vpnfirewall.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vpnfirewall.adapter.AppListAdapter
import com.vpnfirewall.databinding.FragmentAppsBinding
import com.vpnfirewall.viewmodel.MainViewModel

class AppsFragment : Fragment() {
    private var _binding: FragmentAppsBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var appListAdapter: AppListAdapter
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAppsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupObservers()
        setupUI()
    }
    
    private fun setupRecyclerView() {
        appListAdapter = AppListAdapter { appInfo, rule ->
            viewModel.updateAppRule(appInfo.packageName, rule)
        }
        
        binding.recyclerViewApps.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = appListAdapter
        }
    }
    
    private fun setupObservers() {
        viewModel.installedApps.observe(viewLifecycleOwner) { apps ->
            appListAdapter.submitList(apps)
        }
        
        viewModel.filterMode.observe(viewLifecycleOwner) { mode ->
            binding.switchFilterMode.isChecked = mode == MainViewModel.FilterMode.ALLOWLIST
            binding.textFilterMode.text = if (mode == MainViewModel.FilterMode.ALLOWLIST) 
                "Allowlist Mode" else "Blacklist Mode"
        }
    }
    
    private fun setupUI() {
        binding.switchFilterMode.setOnCheckedChangeListener { _, _ ->
            viewModel.toggleFilterMode()
        }
        
        binding.fabVpnToggle.setOnClickListener {
            (requireActivity() as MainActivity).requestVpnPermission()
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}