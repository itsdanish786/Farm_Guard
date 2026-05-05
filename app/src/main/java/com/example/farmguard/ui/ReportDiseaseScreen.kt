package com.example.farmguard.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.farmguard.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDiseaseScreen(
    onSubmitClick: (String, String) -> Unit = { _, _ -> },
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onDashboardClick: () -> Unit = {}
) {
    var sector by remember { mutableStateOf("") }
    var symptoms by remember { mutableStateOf("") }
    var sensorDataEnabled by remember { mutableStateOf(true) }
    var showSectorMenu by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    
    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    val sectors = listOf("North Field", "South Field", "East Orchard", "West Greenhouse", "Aquaculture Tank 1")

    Scaffold(
        bottomBar = { 
            BottomNavigationBar(
                currentScreen = "Reports",
                onHistoryClick = onHistoryClick,
                onDashboardClick = onDashboardClick
            ) 
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { 
                TopHeader(
                    onProfileClick = onProfileClick,
                    onSettingsClick = onSettingsClick
                ) 
            }
            item {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(
                        text = "Report Disease",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Capture anomalies in the field for rapid diagnosis and response.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = FarmGrey
                    )
                }
            }

            // Visual Evidence Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Visual Evidence", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = FarmAlertRed.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    "REQUIRED",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    color = FarmAlertRed,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.background)
                                .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                .clickable { photoLauncher.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedImageUri != null) {
                                Image(
                                    painter = rememberAsyncImagePainter(selectedImageUri),
                                    contentDescription = "Selected image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                // Overlay to allow re-selection
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.BottomEnd
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Change photo",
                                        tint = Color.White,
                                        modifier = Modifier.padding(12.dp).size(24.dp)
                                    )
                                }
                            } else {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text("Tap to capture or upload", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                                    Text(
                                        "Clear, well-lit photos improve diagnostic accuracy.",
                                        color = FarmGrey,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(horizontal = 32.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Farm Sector
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Farm Sector", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.height(12.dp))
                        Box {
                            OutlinedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showSectorMenu = true },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.background),
                                border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = sector.ifEmpty { "Select sector affected..." },
                                        color = if (sector.isEmpty()) FarmGrey else MaterialTheme.colorScheme.onSurface,
                                        fontSize = 14.sp
                                    )
                                    Icon(Icons.Default.ExpandMore, contentDescription = null, tint = FarmGrey)
                                }
                            }
                            DropdownMenu(
                                expanded = showSectorMenu,
                                onDismissRequest = { showSectorMenu = false },
                                modifier = Modifier.fillMaxWidth(0.8f).background(MaterialTheme.colorScheme.surface)
                            ) {
                                sectors.forEach { s ->
                                    DropdownMenuItem(
                                        text = { Text(s) },
                                        onClick = {
                                            sector = s
                                            showSectorMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Environmental Context
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Environmental Context", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Text("Attach recent sensor readings.", color = FarmGrey, fontSize = 12.sp)
                            }
                            Switch(
                                checked = sensorDataEnabled,
                                onCheckedChange = { sensorDataEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = FarmBrightGreen,
                                    uncheckedThumbColor = FarmGrey,
                                    uncheckedTrackColor = Color.LightGray.copy(alpha = 0.3f)
                                )
                            )
                        }
                        if (sensorDataEnabled) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                SensorValueCard(icon = Icons.Default.Thermostat, label = "Temp", value = "24°C", modifier = Modifier.weight(1f))
                                SensorValueCard(icon = Icons.Default.WaterDrop, label = "pH Level", value = "6.8", modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            // Symptom Description
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Symptom Description", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.height(12.dp))
                        TextField(
                            value = symptoms,
                            onValueChange = { symptoms = it },
                            modifier = Modifier.fillMaxWidth().height(120.dp),
                            placeholder = { Text("Describe the physical symptoms, onset timeline, and spread pattern observed...", color = FarmGrey, fontSize = 14.sp) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.background,
                                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            // Submit Button
            item {
                Button(
                    onClick = { onSubmitClick(sector, symptoms) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    enabled = symptoms.isNotEmpty() && sector.isNotEmpty() && selectedImageUri != null
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Submit Report", fontWeight = FontWeight.Bold)
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}


@Composable
fun SensorValueCard(icon: ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(label, color = FarmGrey, fontSize = 10.sp)
                Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}
