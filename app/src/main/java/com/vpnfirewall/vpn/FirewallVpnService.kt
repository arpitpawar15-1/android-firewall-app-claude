package com.vpnfirewall.vpn

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import androidx.core.app.NotificationCompat
import com.vpnfirewall.MainActivity
import com.vpnfirewall.R
import com.vpnfirewall.data.FirewallRule
import com.vpnfirewall.repository.FirewallRepository
import kotlinx.coroutines.*
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.util.concurrent.atomic.AtomicBoolean

class FirewallVpnService : VpnService() {
    companion object {
        private const val VPN_MTU = 1500
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "vpn_firewall_channel"
    }
    
    private var vpnInterface: ParcelFileDescriptor? = null
    private val isRunning = AtomicBoolean(false)
    private lateinit var firewallRepository: FirewallRepository
    private var serviceJob: Job? = null
    private val packetProcessor = PacketProcessor()
    
    override fun onCreate() {
        super.onCreate()
        firewallRepository = FirewallRepository(this)
        createNotificationChannel()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isRunning.get()) {
            startVpn()
        }
        return START_STICKY
    }
    
    private fun startVpn() {
        val builder = Builder()
            .setMtu(VPN_MTU)
            .addAddress("10.0.0.1", 32)
            .addRoute("0.0.0.0", 0)
            .addDnsServer("8.8.8.8")
            .addDnsServer("8.8.4.4")
            .setSession("VPN Firewall")
            .setConfigureIntent(createConfigIntent())
        
        try {
            vpnInterface = builder.establish()
            if (vpnInterface != null) {
                startForeground(NOTIFICATION_ID, createNotification())
                isRunning.set(true)
                startPacketProcessing()
            }
        } catch (e: Exception) {
            stopSelf()
        }
    }
    
    private fun startPacketProcessing() {
        serviceJob = CoroutineScope(Dispatchers.IO).launch {
            val inputStream = FileInputStream(vpnInterface!!.fileDescriptor)
            val outputStream = FileOutputStream(vpnInterface!!.fileDescriptor)
            val packet = ByteBuffer.allocate(VPN_MTU)
            
            while (isRunning.get() && !currentCoroutineContext().isActive.not()) {
                try {
                    val length = inputStream.read(packet.array())
                    if (length > 0) {
                        packet.limit(length)
                        val shouldBlock = packetProcessor.processPacket(packet, firewallRepository)
                        
                        if (!shouldBlock) {
                            outputStream.write(packet.array(), 0, length)
                        }
                        packet.clear()
                    }
                } catch (e: Exception) {
                    break
                }
            }
        }
    }
    
    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("VPN Firewall Active")
            .setContentText("Filtering network traffic")
            .setSmallIcon(R.drawable.ic_shield)
            .setContentIntent(createConfigIntent())
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
    
    private fun createConfigIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(
            this, 0, intent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "VPN Firewall Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows VPN firewall status"
                setShowBadge(false)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    override fun onDestroy() {
        stopVpn()
        super.onDestroy()
    }
    
    private fun stopVpn() {
        isRunning.set(false)
        serviceJob?.cancel()
        vpnInterface?.close()
        vpnInterface = null
    }
}