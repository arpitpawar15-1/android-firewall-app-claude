package com.vpnfirewall.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vpnfirewall.data.FirewallRule
import com.vpnfirewall.data.ProfileType
import com.vpnfirewall.data.VpnProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirewallRepository(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("firewall_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    
    suspend fun getAppRule(packageName: String): FirewallRule? = withContext(Dispatchers.IO) {
        val currentProfile = getCurrentProfile()
        currentProfile.rules[packageName]
    }
    
    suspend fun updateAppRule(packageName: String, rule: FirewallRule) = withContext(Dispatchers.IO) {
        val currentProfile = getCurrentProfile()
        val updatedRules = currentProfile.rules.toMutableMap()
        updatedRules[packageName] = rule
        
        val updatedProfile = currentProfile.copy(rules = updatedRules)
        saveProfile(updatedProfile)
        setCurrentProfile(updatedProfile)
    }
    
    suspend fun getCurrentProfile(): VpnProfile = withContext(Dispatchers.IO) {
        val profileJson = preferences.getString("current_profile", null)
        if (profileJson != null) {
            gson.fromJson(profileJson, VpnProfile::class.java)
        } else {
            getDefaultProfile()
        }
    }
    
    suspend fun setCurrentProfile(profile: VpnProfile) = withContext(Dispatchers.IO) {
        val profileJson = gson.toJson(profile)
        preferences.edit().putString("current_profile", profileJson).apply()
    }
    
    suspend fun getAllProfiles(): List<VpnProfile> = withContext(Dispatchers.IO) {
        val profilesJson = preferences.getString("all_profiles", "[]")
        val type = object : TypeToken<List<VpnProfile>>() {}.type
        gson.fromJson(profilesJson, type) ?: emptyList()
    }
    
    suspend fun saveProfile(profile: VpnProfile) = withContext(Dispatchers.IO) {
        val profiles = getAllProfiles().toMutableList()
        val existingIndex = profiles.indexOfFirst { it.id == profile.id }
        
        if (existingIndex != -1) {
            profiles[existingIndex] = profile
        } else {
            profiles.add(profile)
        }
        
        val profilesJson = gson.toJson(profiles)
        preferences.edit().putString("all_profiles", profilesJson).apply()
    }
    
    private fun getDefaultProfile(): VpnProfile {
        return VpnProfile(
            id = "default",
            name = "Default Profile",
            description = "Default firewall settings",
            isDefault = true,
            profileType = ProfileType.CUSTOM
        )
    }
}