package com.example.farmguard

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.farmguard.ui.RegisterScreen
import com.example.farmguard.ui.theme.FarmGuardTheme
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        auth = FirebaseAuth.getInstance()

        setContent {
            FarmGuardTheme {
                RegisterScreen(
                    onRegisterClick = { email, password ->
                        if (email.isEmpty() || password.isEmpty()) {
                            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        } else if (password.length < 6) {
                            Toast.makeText(this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show()
                        } else {
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, MainActivity::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    },
                    onLoginClick = {
                        finish()
                    }
                )
            }
        }
    }
}
