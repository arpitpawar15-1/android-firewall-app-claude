package com.vpnfirewall.repository

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.vpnfirewall.data.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val context: Context) {
    
    suspend fun getInstalledApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        val packageManager = context.packageManager
        val packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        
        packages.filter { hasInternetPermission(it.packageName) }
            .map { appInfo ->
                AppInfo(
                    packageName = appInfo.packageName,
                    appName = getAppName(appInfo),
                    isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0,
                    hasInternetPermission = true
                )
            }
            .sortedBy { it.appName }
    }
    
    private fun hasInternetPermission(packageName: String): Boolean {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(
                packageName, 
                PackageManager.GET_PERMISSIONS
            )
            packageInfo.requestedPermissions?.contains("android.permission.INTERNET") == true
        } catch (e: Exception) {
            false
        }
    }
    
    private fun getAppName(appInfo: ApplicationInfo): String {
        return context.packageManager.getApplicationLabel(appInfo).toString()
    }
}