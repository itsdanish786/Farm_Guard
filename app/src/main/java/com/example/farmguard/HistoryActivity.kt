package com.example.farmguard

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.farmguard.ui.*
import com.example.farmguard.ui.theme.FarmGuardTheme

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FarmGuardTheme {
                var showProfileMenu by remember { mutableStateOf(false) }
                var showSettingsMenu by remember { mutableStateOf(false) }

                Box(modifier = Modifier.fillMaxSize()) {
                    HistoryScreen(
                        onBackClick = {
                            finish()
                        },
                        onDashboardClick = {
                            finish() // Go back to MainActivity
                        },
                        onProfileClick = { showProfileMenu = true },
                        onSettingsClick = { showSettingsMenu = true }
                    )

                    if (showProfileMenu) {
                        ProfileMenu(
                            onDismiss = { showProfileMenu = false },
                            onLogout = {
                                showProfileMenu = false
                                finish()
                            }
                        )
                    }

                    if (showSettingsMenu) {
                        SettingsMenu(
                            onDismiss = { showSettingsMenu = false }
                        )
                    }
                }
            }
        }
    }
}
