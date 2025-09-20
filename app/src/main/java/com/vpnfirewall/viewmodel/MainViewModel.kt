package com.vpnfirewall.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vpnfirewall.data.AppInfo
import com.vpnfirewall.data.FirewallRule
import com.vpnfirewall.data.VpnProfile
import com.vpnfirewall.repository.AppRepository
import com.vpnfirewall.repository.FirewallRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val appRepository = AppRepository(application)
    private val firewallRepository = FirewallRepository(application)
    
    private val _installedApps = MutableLiveData<List<AppInfo>>()
    val installedApps: LiveData<List<AppInfo>> = _installedApps
    
    private val _vpnStatus = MutableLiveData<Boolean>()
    val vpnStatus: LiveData<Boolean> = _vpnStatus
    
    private val _currentProfile = MutableLiveData<VpnProfile>()
    val currentProfile: LiveData<VpnProfile> = _currentProfile
    
    private val _filterMode = MutableLiveData<FilterMode>()
    val filterMode: LiveData<FilterMode> = _filterMode
    
    init {
        loadInstalledApps()
        loadCurrentProfile()
        _filterMode.value = FilterMode.BLACKLIST
    }
    
    private fun loadInstalledApps() {
        viewModelScope.launch {
            _installedApps.value = appRepository.getInstalledApps()
        }
    }
    
    private fun loadCurrentProfile() {
        viewModelScope.launch {
            _currentProfile.value = firewallRepository.getCurrentProfile()
        }
    }
    
    fun updateAppRule(packageName: String, rule: FirewallRule) {
        viewModelScope.launch {
            firewallRepository.updateAppRule(packageName, rule)
        }
    }
    
    fun switchProfile(profile: VpnProfile) {
        viewModelScope.launch {
            firewallRepository.setCurrentProfile(profile)
            _currentProfile.value = profile
        }
    }
    
    fun toggleFilterMode() {
        _filterMode.value = when (_filterMode.value) {
            FilterMode.BLACKLIST -> FilterMode.ALLOWLIST
            FilterMode.ALLOWLIST -> FilterMode.BLACKLIST
            null -> FilterMode.BLACKLIST
        }
    }
    
    enum class FilterMode {
        BLACKLIST, ALLOWLIST
    }
}