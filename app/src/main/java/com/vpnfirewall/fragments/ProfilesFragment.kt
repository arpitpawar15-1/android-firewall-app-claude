package com.vpnfirewall.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vpnfirewall.adapter.ProfileAdapter
import com.vpnfirewall.data.ProfileType
import com.vpnfirewall.data.VpnProfile
import com.vpnfirewall.databinding.FragmentProfilesBinding
import com.vpnfirewall.viewmodel.MainViewModel
import java.util.UUID

class ProfilesFragment : Fragment() {
    private var _binding: FragmentProfilesBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var profileAdapter: ProfileAdapter
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfilesBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupObservers()
        setupPredefinedProfiles()
    }
    
    private fun setupRecyclerView() {
        profileAdapter = ProfileAdapter { profile ->
            viewModel.switchProfile(profile)
        }
        
        binding.recyclerViewProfiles.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = profileAdapter
        }
    }
    
    private fun setupObservers() {
        viewModel.currentProfile.observe(viewLifecycleOwner) { currentProfile ->
            binding.textCurrentProfile.text = "Active: ${currentProfile.name}"
        }
    }
    
    private fun setupPredefinedProfiles() {
        val profiles = listOf(
            VpnProfile(
                id = UUID.randomUUID().toString(),
                name = "Work Profile",
                description = "Strict filtering for work environment",
                profileType = ProfileType.WORK
            ),
            VpnProfile(
                id = UUID.randomUUID().toString(),
                name = "Personal Profile",
                description = "Balanced filtering for personal use",
                profileType = ProfileType.PERSONAL
            ),
            VpnProfile(
                id = UUID.randomUUID().toString(),
                name = "Private Mode",
                description = "Maximum privacy and security",
                profileType = ProfileType.PRIVATE
            )
        )
        
        profileAdapter.submitList(profiles)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}