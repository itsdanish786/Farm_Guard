package com.example.farmguard.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.farmguard.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    var progress by remember { mutableStateOf(0f) }
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f, 
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing), 
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    LaunchedEffect(Unit) {
        val duration = 3500L
        val startTime = System.currentTimeMillis()
        while (progress < 1f) {
            val elapsed = System.currentTimeMillis() - startTime
            progress = (elapsed.toFloat() / duration).coerceAtMost(1f)
            delay(16)
        }
        delay(600)
        onTimeout()
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF050705))) {
        GridBackground()

        // --- TOP TELEMETRY BAR ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Wifi, contentDescription = null, tint = FarmBrightGreen, modifier = Modifier.size(20.dp))
            Text(
                text = "CORE_TELEMETRY",
                color = FarmBrightGreen,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Icon(Icons.Default.Tune, contentDescription = null, tint = FarmBrightGreen, modifier = Modifier.size(20.dp))
        }

        // --- CENTRAL INITIALIZING BOX ---
        Box(modifier = Modifier.align(Alignment.Center)) {
            // Faint background dashed border
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .border(1.dp, FarmBrightGreen.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
            )

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Glowing Central Logo (Stylized Circle)
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .blur(15.dp)
                            .background(FarmBrightGreen.copy(alpha = glowAlpha), CircleShape)
                    )
                    Icon(
                        imageVector = Icons.Default.RadioButtonChecked, 
                        contentDescription = null, 
                        tint = FarmBrightGreen, 
                        modifier = Modifier.size(50.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "SYSTEM_INITIALIZING",
                    color = FarmBrightGreen,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Small Gradient Progress Bar
                Box(modifier = Modifier.width(120.dp).height(4.dp).background(Color.White.copy(alpha = 0.1f), CircleShape)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .background(
                                brush = Brush.horizontalGradient(listOf(FarmBrightGreen, Color(0xFF3B82F6))), 
                                shape = CircleShape
                            )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "SEQ: 0x8F2A", 
                    color = FarmBrightGreen.copy(alpha = 0.5f), 
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            // --- FLOATING CARDS ---
            TelemetryCard(
                modifier = Modifier.offset(x = (-60).dp, y = (-120).dp),
                label = "TEMP", 
                value = "24°C", 
                icon = Icons.Default.Thermostat
            )
            TelemetryCard(
                modifier = Modifier.offset(x = 160.dp, y = (-40).dp),
                label = "HUMIDITY", 
                value = "65%", 
                icon = Icons.Default.WaterDrop
            )
            StatusCard(
                modifier = Modifier.align(Alignment.BottomCenter).offset(y = 80.dp),
                label = "PUMP_STATUS", 
                status = "ACTIVE"
            )
        }

        // --- BOTTOM BRANDING ---
        Text(
            text = "FARM GUARD",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp),
            color = FarmBrightGreen,
            fontSize = 42.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 4.sp
        )
    }
}

@Composable
fun TelemetryCard(modifier: Modifier, label: String, value: String, icon: ImageVector) {
    Box(
        modifier = modifier
            .width(110.dp)
            .background(Color(0xFF151815).copy(alpha = 0.9f), RoundedCornerShape(4.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = Color(0xFF3B82F6), modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = label, color = FarmGrey, fontSize = 8.sp, fontWeight = FontWeight.Bold)
            }
            Text(text = value, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Light)
        }
    }
}

@Composable
fun StatusCard(modifier: Modifier, label: String, status: String) {
    Box(
        modifier = modifier
            .width(160.dp)
            .background(Color(0xFF151815).copy(alpha = 0.9f), RoundedCornerShape(4.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(), 
                horizontalArrangement = Arrangement.SpaceBetween, 
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = label, color = FarmGrey, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(FarmBrightGreen, CircleShape)
                        .blur(2.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = status, color = FarmBrightGreen, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
fun GridBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val gridSize = 32.dp.toPx()
        val color = Color.White.copy(alpha = 0.03f)
        
        var x = 0f
        while (x < size.width) {
            drawLine(color, Offset(x, 0f), Offset(x, size.height), 1f)
            x += gridSize
        }
        
        var y = 0f
        while (y < size.height) {
            drawLine(color, Offset(0f, y), Offset(size.width, y), 1f)
            y += gridSize
        }
    }
}
