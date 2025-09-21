package com.vpnfirewall.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.vpnfirewall.vpn.FirewallVpnService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED,
            Intent.ACTION_PACKAGE_REPLACED -> {
                // Auto-start VPN service if it was previously enabled
                val prefs = context.getSharedPreferences("vpn_prefs", Context.MODE_PRIVATE)
                val wasEnabled = prefs.getBoolean("vpn_auto_start", false)
                if (wasEnabled) {
                    val serviceIntent = Intent(context, FirewallVpnService::class.java)
                    context.startForegroundService(serviceIntent)
                }
            }
        }
    }
}