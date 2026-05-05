package com.example.farmguard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.farmguard.ui.SplashScreen
import com.example.farmguard.ui.theme.FarmGuardTheme
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            FarmGuardTheme {
                SplashScreen(onTimeout = {
                    val auth = FirebaseAuth.getInstance()
                    val nextActivity = if (auth.currentUser != null) {
                        MainActivity::class.java
                    } else {
                        LoginActivity::class.java
                    }
                    startActivity(Intent(this@SplashActivity, nextActivity))
                    finish()
                })
            }
        }
    }
}
