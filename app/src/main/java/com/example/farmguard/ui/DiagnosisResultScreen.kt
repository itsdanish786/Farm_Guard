package com.example.farmguard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.farmguard.ui.theme.*

@Composable
fun DiagnosisResultScreen(
    diseaseName: String = "Wheat Rust (Puccinia striiformis)",
    confidence: Float = 0.85f,
    onBackHome: () -> Unit = {},
    onFindExpert: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onReportsClick: () -> Unit = {}
) {
    Scaffold(
        bottomBar = { 
            BottomNavigationBar(
                currentScreen = "Reports", 
                onDashboardClick = onBackHome,
                onHistoryClick = onHistoryClick,
                onReportsClick = onReportsClick
            ) 
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopHeader(
                onProfileClick = onProfileClick,
                onSettingsClick = onSettingsClick
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.secondary),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(40.dp))
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Diagnosis Complete",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Scan processed successfully.",
                style = MaterialTheme.typography.bodyMedium,
                color = FarmGrey
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("LIKELY DISEASE", color = FarmGrey, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = diseaseName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("CONFIDENCE", color = FarmGrey, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LinearProgressIndicator(
                            progress = { confidence },
                            modifier = Modifier
                                .weight(1f)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = Color.LightGray.copy(alpha = 0.3f)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("${(confidence * 100).toInt()}%", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Place, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("RECOMMENDED ACTION", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Immediate application of a systemic fungicide is recommended. Isolate affected areas if possible to prevent wind dispersal of spores. Monitor surrounding fields closely over the next 48 hours.",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = onFindExpert,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Find Nearby Expert", fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedButton(
                onClick = onBackHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Back to Home", fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Text(
                text = "Disclaimer: This diagnosis is generated by an automated system based on visual analysis and should not replace professional agronomic advice. Always consult with a certified crop advisor or local agricultural extension office before taking significant action.",
                style = MaterialTheme.typography.bodySmall,
                color = FarmGrey,
                textAlign = TextAlign.Center,
                fontSize = 10.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}
