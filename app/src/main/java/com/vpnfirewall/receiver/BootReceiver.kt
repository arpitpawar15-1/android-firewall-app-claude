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
}="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:padding="16dp"
                android:gravity="center_vertical">

                <View
                    android:id="@+id/indicator_vpn_status"
                    android:layout_width="12dp"
                    android:layout_height="12dp"
                    android:background="@drawable/circle_indicator" />

                <TextView
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:layout_marginStart="12dp"
                    android:text="VPN Status"
                    android:textAppearance="@style/TextAppearance.Material3.BodyLarge" />

                <TextView
                    android:id="@+id/text_vpn_status"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Inactive"
                    android:textAppearance="@style/TextAppearance.Material3.BodyLarge"
                    android:textStyle="bold" />

            </LinearLayout>

        </com.google.android.material.card.MaterialCardView>

        <!-- Statistics Grid -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:layout_marginBottom="16dp">

                <com.google.android.material.card.MaterialCardView
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:layout_marginEnd="8dp"
                    app:cardCornerRadius="12dp">

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:orientation="vertical"
                        android:padding="16dp"
                        android:gravity="center">

                        <TextView
                            android:id="@+id/text_blocked_requests"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="0"
                            android:textAppearance="@style/TextAppearance.Material3.HeadlineLarge"
                            android:textStyle="bold"
                            android:textColor="?attr/colorError" />

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="4dp"
                            android:text="Blocked"
                            android:textAppearance="@style/TextAppearance.Material3.BodyMedium" />

                    </LinearLayout>

                </com.google.android.material.card.MaterialCardView>

                <com.google.android.material.card.MaterialCardView
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:layout_marginStart="8dp"
                    app:cardCornerRadius="12dp">

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:orientation="vertical"
                        android:padding="16dp"
                        android:gravity="center">

                        <TextView
                            android:id="@+id/text_allowed_requests"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="0"
                            android:textAppearance="@style/TextAppearance.Material3.HeadlineLarge"
                            android:textStyle="bold"
                            android:textColor="?attr/colorPrimary" />

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="4dp"
                            android:text="Allowed"
                            android:textAppearance="@style/TextAppearance.Material3.BodyMedium" />

                    </LinearLayout>

                </com.google.android.material.card.MaterialCardView>

            </LinearLayout>

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal">

                <com.google.android.material.card.MaterialCardView
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:layout_marginEnd="8dp"
                    app:cardCornerRadius="12dp">

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:orientation="vertical"
                        android:padding="16dp"
                        android:gravity="center">

                        <TextView
                            android:id="@+id/text_total_data_saved"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="0 MB"
                            android:textAppearance="@style/TextAppearance.Material3.HeadlineMedium"
                            android:textStyle="bold" />

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="4dp"
                            android:text="Data Saved"
                            android:textAppearance="@style/TextAppearance.Material3.BodyMedium" />

                    </LinearLayout>

                </com.google.android.material.card.MaterialCardView>

                <com.google.android.material.card.MaterialCardView
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:layout_marginStart="8dp"
                    app:cardCornerRadius="12dp">

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:orientation="vertical"
                        android:padding="16dp"
                        android:gravity="center">

                        <TextView
                            android:id="@+id/text_active_apps"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="0"
                            android:textAppearance="@style/TextAppearance.Material3.HeadlineMedium"
                            android:textStyle="bold" />

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="4dp"
                            android:text="Active Apps"
                            android:textAppearance="@style/TextAppearance.Material3.BodyMedium" />

                    </LinearLayout>

                </com.google.android.material.card.MaterialCardView>

            </LinearLayout>

        </LinearLayout>

        <!-- Real-time Monitoring Toggle -->
        <com.google.android.material.card.MaterialCardView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="24dp"
            app:cardCornerRadius="12dp">

            <LinearLayout
                android:layout_width        return null
    }
}