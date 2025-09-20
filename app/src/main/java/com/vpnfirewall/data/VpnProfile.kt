package com.vpnfirewall.data

data class VpnProfile(
    val id: String,
    val name: String,
    val description: String = "",
    val rules: Map<String, FirewallRule> = emptyMap(),
    val isDefault: Boolean = false,
    val profileType: ProfileType = ProfileType.CUSTOM,
    val createdAt: Long = System.currentTimeMillis()
)

enum class ProfileType {
    WORK, PERSONAL, PRIVATE, CUSTOM
}