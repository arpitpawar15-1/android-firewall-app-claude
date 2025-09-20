package com.vpnfirewall.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vpnfirewall.data.AppInfo
import com.vpnfirewall.data.FirewallRule
import com.vpnfirewall.databinding.ItemAppBinding

class AppListAdapter(
    private val onRuleChanged: (AppInfo, FirewallRule) -> Unit
) : ListAdapter<AppInfo, AppListAdapter.AppViewHolder>(AppDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val binding = ItemAppBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class AppViewHolder(private val binding: ItemAppBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(appInfo: AppInfo) {
            binding.textAppName.text = appInfo.appName
            binding.textPackageName.text = appInfo.packageName
            
            // Set current rule state
            val currentRule = getCurrentRule(appInfo.packageName)
            
            binding.switchWifi.isChecked = currentRule.allowWifi
            binding.switchMobile.isChecked = currentRule.allowMobile
            binding.switchMaster.isChecked = currentRule.masterOverride
            
            // Setup listeners
            binding.switchWifi.setOnCheckedChangeListener { _, isChecked ->
                val newRule = currentRule.copy(allowWifi = isChecked)
                onRuleChanged(appInfo, newRule)
            }
            
            binding.switchMobile.setOnCheckedChangeListener { _, isChecked ->
                val newRule = currentRule.copy(allowMobile = isChecked)
                onRuleChanged(appInfo, newRule)
            }
            
            binding.switchMaster.setOnCheckedChangeListener { _, isChecked ->
                val newRule = currentRule.copy(masterOverride = isChecked)
                onRuleChanged(appInfo, newRule)
            }
            
            // Expand/collapse for domain filtering
            binding.cardApp.setOnClickListener {
                // TODO: Toggle domain filtering section
            }
        }
        
        private fun getCurrentRule(packageName: String): FirewallRule {
            // Default rule if none exists
            return FirewallRule(packageName = packageName)
        }
    }
    
    class AppDiffCallback : DiffUtil.ItemCallback<AppInfo>() {
        override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem.packageName == newItem.packageName
        }
        
        override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem == newItem
        }
    }
}