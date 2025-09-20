package com.vpnfirewall

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vpnfirewall.databinding.ActivityMainBinding
import com.vpnfirewall.fragments.AppsFragment
import com.vpnfirewall.fragments.ProfilesFragment
import com.vpnfirewall.fragments.StatsFragment
import com.vpnfirewall.viewmodel.MainViewModel
import com.vpnfirewall.vpn.FirewallVpnService

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    
    private val vpnPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            startVpnService()
        } else {
            Toast.makeText(this, "VPN permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        
        setupBottomNavigation()
        setupObservers()
        
        // Load initial fragment
        if (savedInstanceState == null) {
            loadFragment(AppsFragment())
        }
    }
    
    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_apps -> {
                    loadFragment(AppsFragment())
                    true
                }
                R.id.nav_profiles -> {
                    loadFragment(ProfilesFragment())
                    true
                }
                R.id.nav_stats -> {
                    loadFragment(StatsFragment())
                    true
                }
                else -> false
            }
        }
    }
    
    private fun setupObservers() {
        viewModel.vpnStatus.observe(this) { isRunning ->
            // Update UI based on VPN status
        }
    }
    
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    
    fun requestVpnPermission() {
        val intent = VpnService.prepare(this)
        if (intent != null) {
            vpnPermissionLauncher.launch(intent)
        } else {
            startVpnService()
        }
    }
    
    private fun startVpnService() {
        val intent = Intent(this, FirewallVpnService::class.java)
        startForegroundService(intent)
    }
}