package com.vpnfirewall.data

data class FirewallRule(
    val packageName: String,
    val allowWifi: Boolean = true,
    val allowMobile: Boolean = true,
    val masterOverride: Boolean = true,
    val blockedDomains: Set<String> = emptySet(),
    val allowedDomains: Set<String> = emptySet(),
    val lastModified: Long = System.currentTimeMillis()
) {
    fun isBlocked(connectionType: ConnectionType): Boolean {
        if (!masterOverride) return true
        
        return when (connectionType) {
            ConnectionType.WIFI -> !allowWifi
            ConnectionType.MOBILE -> !allowMobile
            ConnectionType.UNKNOWN -> !masterOverride
        }
    }
}

enum class ConnectionType {
    WIFI, MOBILE, UNKNOWN
}