package com.example.farmguard

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.farmguard.ui.*
import com.example.farmguard.ui.theme.FarmGuardTheme

class ReportDiseaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FarmGuardTheme {
                var showResult by remember { mutableStateOf(false) }

                var showProfileMenu by remember { mutableStateOf(false) }
                var showSettingsMenu by remember { mutableStateOf(false) }

                Box(modifier = Modifier.fillMaxSize()) {
                    if (!showResult) {
                        ReportDiseaseScreen(
                            onSubmitClick = { sector, symptoms ->
                                showResult = true
                            },
                            onBackClick = {
                                finish()
                            },
                            onProfileClick = { showProfileMenu = true },
                            onSettingsClick = { showSettingsMenu = true },
                            onHistoryClick = {
                                startActivity(Intent(this@ReportDiseaseActivity, HistoryActivity::class.java))
                                finish()
                            },
                            onDashboardClick = {
                                finish()
                            }
                        )
                    } else {
                        DiagnosisResultScreen(
                            onBackHome = {
                                finish()
                            },
                            onFindExpert = {
                                // Logic to open maps or expert contact
                            },
                            onProfileClick = { showProfileMenu = true },
                            onSettingsClick = { showSettingsMenu = true },
                            onHistoryClick = {
                                startActivity(Intent(this@ReportDiseaseActivity, HistoryActivity::class.java))
                                finish()
                            },
                            onReportsClick = {
                                showResult = false
                            }
                        )
                    }

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
