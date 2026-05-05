package com.example.farmguard.models

import com.google.firebase.Timestamp

data class Report(
    val id: String = "",
    val userUID: String = "",
    val farmType: String = "",
    val symptoms: String = "",
    val diagnosisResult: String = "",
    val confidence: Double = 0.0,
    val imageUrl: String = "",
    val preventiveAdvice: String = "",
    val timestamp: Timestamp = Timestamp.now()
) {
    // Empty constructor for Firestore
    constructor() : this("", "", "", "", "", 0.0, "", "", Timestamp.now())
}