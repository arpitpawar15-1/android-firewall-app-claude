package com.vpnfirewall.data

data class AppInfo(
    val packageName: String,
    val appName: String,
    val iconResId: Int? = null,
    val isSystemApp: Boolean = false,
    val hasInternetPermission: Boolean = true,
    val dataUsage: DataUsage = DataUsage()
)

data class DataUsage(
    val wifiBytes: Long = 0L,
    val mobileBytes: Long = 0L,
    val totalBytes: Long = 0L,
    val lastUpdated: Long = System.currentTimeMillis()
)