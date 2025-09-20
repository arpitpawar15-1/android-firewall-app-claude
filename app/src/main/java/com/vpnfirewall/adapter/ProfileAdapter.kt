package com.vpnfirewall.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vpnfirewall.data.ProfileType
import com.vpnfirewall.data.VpnProfile
import com.vpnfirewall.databinding.ItemProfileBinding

class ProfileAdapter(
    private val onProfileSelected: (VpnProfile) -> Unit
) : ListAdapter<VpnProfile, ProfileAdapter.ProfileViewHolder>(ProfileDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ProfileViewHolder(private val binding: ItemProfileBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(profile: VpnProfile) {
            binding.textProfileName.text = profile.name
            binding.textProfileDescription.text = profile.description
            
            // Set profile type icon
            val iconRes = when (profile.profileType) {
                ProfileType.WORK -> android.R.drawable.ic_menu_agenda
                ProfileType.PERSONAL -> android.R.drawable.ic_menu_myplaces
                ProfileType.PRIVATE -> android.R.drawable.ic_menu_view
                ProfileType.CUSTOM -> android.R.drawable.ic_menu_edit
            }
            binding.imageProfileIcon.setImageResource(iconRes)
            
            binding.cardProfile.setOnClickListener {
                onProfileSelected(profile)
            }
        }
    }
    
    class ProfileDiffCallback : DiffUtil.ItemCallback<VpnProfile>() {
        override fun areItemsTheSame(oldItem: VpnProfile, newItem: VpnProfile): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: VpnProfile, newItem: VpnProfile): Boolean {
            return oldItem == newItem
        }
    }
}