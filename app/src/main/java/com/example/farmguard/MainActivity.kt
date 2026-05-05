package com.example.farmguard

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.farmguard.ui.DashboardScreen
import com.example.farmguard.ui.theme.FarmGuardTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        auth = FirebaseAuth.getInstance()

        setContent {
            FarmGuardTheme {
                DashboardScreen(
                    onReportClick = {
                        startActivity(Intent(this, ReportDiseaseActivity::class.java))
                    },
                    onHistoryClick = {
                        startActivity(Intent(this, HistoryActivity::class.java))
                    },
                    onDashboardClick = {
                        // Already on Dashboard
                    },
                    onLogoutClick = {
                        auth.signOut()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}
