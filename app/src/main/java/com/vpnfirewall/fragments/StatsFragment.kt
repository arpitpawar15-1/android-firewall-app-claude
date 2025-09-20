package com.vpnfirewall.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.vpnfirewall.databinding.FragmentStatsBinding
import com.vpnfirewall.viewmodel.MainViewModel

class StatsFragment : Fragment() {
    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: MainViewModel by activityViewModels()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupObservers()
        updateStats()
    }
    
    private fun setupObservers() {
        viewModel.vpnStatus.observe(viewLifecycleOwner) { isRunning ->
            binding.textVpnStatus.text = if (isRunning) "Active" else "Inactive"
            binding.indicatorVpnStatus.setBackgroundColor(
                if (isRunning) 0xFF4CAF50.toInt() else 0xFFF44336.toInt()
            )
        }
    }
    
    private fun updateStats() {
        // Mock data - would be replaced with real statistics
        binding.textBlockedRequests.text = "1,247"
        binding.textAllowedRequests.text = "8,931"
        binding.textTotalDataSaved.text = "145 MB"
        binding.textActiveApps.text = "23"
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}