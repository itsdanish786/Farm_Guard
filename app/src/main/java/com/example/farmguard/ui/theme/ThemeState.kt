package com.example.farmguard.ui.theme

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object ThemeState {
    var isDarkMode by mutableStateOf(false)
    var language by mutableStateOf("English")
    var userName by mutableStateOf("Elias Farmer")
    var profileImageUri by mutableStateOf<Uri?>(null)
}
