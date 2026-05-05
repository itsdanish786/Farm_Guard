package com.example.farmguard.ui

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.farmguard.ui.theme.*
import com.example.farmguard.viewmodel.WeatherViewModel

@Composable
fun DashboardScreen(
    onReportClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onDashboardClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    weatherViewModel: WeatherViewModel = viewModel()
) {
    var showProfileMenu by remember { mutableStateOf(false) }
    var showSettingsMenu by remember { mutableStateOf(false) }
    var showEditProfile by remember { mutableStateOf(false) }
    var showChangePassword by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            weatherViewModel.fetchWeatherByLocation()
        } else {
            weatherViewModel.fetchWeatherByCity("London")
        }
    }

    LaunchedEffect(Unit) {
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (fineLocationPermission == PackageManager.PERMISSION_GRANTED ||
            coarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            weatherViewModel.fetchWeatherByLocation()
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Scaffold(
        bottomBar = { 
            BottomNavigationBar(
                currentScreen = "Dashboard", 
                onHistoryClick = onHistoryClick, 
                onReportsClick = onReportClick,
                onDashboardClick = onDashboardClick
            ) 
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
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
                        onProfileClick = { showProfileMenu = true },
                        onSettingsClick = { showSettingsMenu = true }
                    ) 
                }
                item { GreetingSection(name = ThemeState.userName.split(" ")[0]) }
                item { 
                    WeatherCard(
                        weatherViewModel = weatherViewModel,
                        onRefresh = { weatherViewModel.fetchWeatherByLocation() }
                    ) 
                }
                item { 
                    StatusCard(
                        title = "Crop Health",
                        subtitle = "Soil moisture optimal in Sectors A & B.",
                        status = "Normal",
                        statusColor = FarmBrightGreen,
                        icon = Icons.Default.Eco,
                        progress = 0.75f
                    )
                }
                item {
                    StatusCard(
                        title = "Livestock Health",
                        subtitle = "Irregular movement detected in Herd 3.",
                        status = "Attention",
                        statusColor = FarmAlertRed,
                        icon = Icons.Default.Pets,
                        progress = 0.4f
                    )
                }
                item {
                    StatusCard(
                        title = "Aquaculture",
                        subtitle = "Ph levels stable across main tanks.",
                        status = "Normal",
                        statusColor = FarmBlue,
                        icon = Icons.Default.WaterDrop,
                        progress = 0.9f
                    )
                }
                item { SensorStatusCard() }
                item { ExpertSupportBanner() }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }

            if (showProfileMenu) {
                ProfileMenu(
                    onDismiss = { showProfileMenu = false },
                    onLogout = { 
                        showProfileMenu = false
                        onLogoutClick()
                    },
                    onEditProfile = {
                        showProfileMenu = false
                        showEditProfile = true
                    },
                    onChangePassword = {
                        showProfileMenu = false
                        showChangePassword = true
                    }
                )
            }

            if (showSettingsMenu) {
                SettingsMenu(
                    onDismiss = { showSettingsMenu = false }
                )
            }

            if (showEditProfile) {
                EditProfileDialog(
                    onDismiss = { showEditProfile = false }
                )
            }

            if (showChangePassword) {
                ChangePasswordDialog(
                    onDismiss = { showChangePassword = false }
                )
            }
        }
    }
}

@Composable
fun TopHeader(onProfileClick: () -> Unit, onSettingsClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Agriculture,
                contentDescription = "Logo",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Farm Guard",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onSettingsClick) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = FarmGrey)
            }
            Spacer(modifier = Modifier.width(4.dp))
            Surface(
                onClick = onProfileClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                color = Color.LightGray
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (ThemeState.profileImageUri != null) {
                        AsyncImage(
                            model = ThemeState.profileImageUri,
                            contentDescription = "Profile",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileMenu(onDismiss: () -> Unit, onLogout: () -> Unit, onEditProfile: () -> Unit = {}, onChangePassword: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .padding(24.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        Card(
            modifier = Modifier
                .width(240.dp)
                .padding(top = 40.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        if (ThemeState.profileImageUri != null) {
                            AsyncImage(
                                model = ThemeState.profileImageUri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(ThemeState.userName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                        Text("Premium Member", color = FarmGrey, fontSize = 12.sp)
                    }
                }
                
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 0.5.dp)
                
                ProfileMenuItem(Icons.Default.Edit, "Edit Profile") {
                    onEditProfile()
                }
                ProfileMenuItem(Icons.Default.Lock, "Change Password") {
                    onChangePassword()
                }
                ProfileMenuItem(Icons.Default.Payment, "Subscription") {
                    onDismiss()
                }
                
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp)
                
                ProfileMenuItem(Icons.Default.Logout, "Logout", color = FarmAlertRed) {
                    onLogout()
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close", color = FarmGrey)
                }
            }
        }
    }
}

@Composable
fun EditProfileDialog(onDismiss: () -> Unit) {
    var name by remember { mutableStateOf(ThemeState.userName) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            ThemeState.profileImageUri = uri
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Edit Profile", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(24.dp))
                
                Box(
                    modifier = Modifier.size(100.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer).clickable { launcher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (ThemeState.profileImageUri != null) {
                        AsyncImage(
                            model = ThemeState.profileImageUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(Icons.Default.AddAPhoto, contentDescription = null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
                    }
                }
                Text("Tap to change picture", style = MaterialTheme.typography.bodySmall, color = FarmGrey, modifier = Modifier.padding(top = 8.dp))
                
                Spacer(modifier = Modifier.height(24.dp))
                
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Display Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = {
                        ThemeState.userName = name
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Save Changes")
                }
            }
        }
    }
}

@Composable
fun ChangePasswordDialog(onDismiss: () -> Unit) {
    var oldPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Change Password", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(24.dp))
                
                OutlinedTextField(value = oldPass, onValueChange = { oldPass = it }, label = { Text("Old Password") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = newPass, onValueChange = { newPass = it }, label = { Text("New Password") }, modifier = Modifier.fillMaxWidth())
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Update Password")
                }
            }
        }
    }
}

@Composable
fun ProfileMenuItem(icon: ImageVector, label: String, color: Color = MaterialTheme.colorScheme.onSurface, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, color = color, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun SettingsMenu(onDismiss: () -> Unit) {
    var notificationsEnabled by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .padding(24.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        Card(
            modifier = Modifier
                .width(280.dp)
                .padding(top = 40.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "App Settings",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                SettingsToggleItem(
                    icon = Icons.Default.DarkMode,
                    label = "Dark Mode",
                    checked = ThemeState.isDarkMode,
                    onCheckedChange = { ThemeState.isDarkMode = it }
                )
                
                SettingsToggleItem(
                    icon = Icons.Default.NotificationsActive,
                    label = "Push Notifications",
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )
                
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp)
                
                ProfileMenuItem(
                    icon = Icons.Default.Language, 
                    label = "Language: ${ThemeState.language}",
                    onClick = {
                        ThemeState.language = if (ThemeState.language == "English") "Spanish" else "English"
                    }
                )
                ProfileMenuItem(Icons.Default.Info, "About Farm Guard") {}
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Done")
                }
            }
        }
    }
}

@Composable
fun SettingsToggleItem(
    icon: ImageVector,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = FarmGrey, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = FarmBrightGreen,
                uncheckedThumbColor = FarmGrey,
                uncheckedTrackColor = Color.LightGray
            )
        )
    }
}

@Composable
fun GreetingSection(name: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "Morning, $name",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Here is your farm's overview for today.",
            style = MaterialTheme.typography.bodyMedium,
            color = FarmGrey
        )
    }
}

@Composable
fun WeatherCard(
    weatherViewModel: WeatherViewModel,
    onRefresh: () -> Unit
) {
    val weather = weatherViewModel.weatherResponse
    val cityName = weatherViewModel.cityName
    val isLoading = weatherViewModel.isLoading
    val error = weatherViewModel.errorMessage

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRefresh() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3498DB))
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(32.dp))
            } else {
                Icon(
                    imageVector = when {
                        weather == null -> Icons.Default.CloudOff
                        weather.current.weatherCode == 0 -> Icons.Default.WbSunny
                        weather.current.weatherCode in 1..3 -> Icons.Default.WbCloudy
                        weather.current.weatherCode in 61..65 -> Icons.Default.WaterDrop
                        else -> Icons.Default.Cloud
                    },
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                if (isLoading) {
                    Text("Updating weather...", color = Color.White, fontWeight = FontWeight.Medium)
                } else if (weather != null) {
                    Text(cityName, color = Color.White, fontWeight = FontWeight.Bold)
                    Text(
                        "${weather.current.temperature.toInt()}°C • ${weatherViewModel.getWeatherDescription(weather.current.weatherCode)} • ${weather.current.humidity}% Humidity",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                } else {
                    Text("Weather Unavailable", color = Color.White, fontWeight = FontWeight.Bold)
                    Text(error ?: "Tap to retry", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                }
            }
            
            if (!isLoading) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun StatusCard(
    title: String,
    subtitle: String,
    status: String,
    statusColor: Color,
    icon: ImageVector,
    progress: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
                Surface(
                    shape = CircleShape,
                    color = statusColor.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(statusColor))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(status, color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
            Text(subtitle, color = FarmGrey, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                color = statusColor,
                trackColor = Color.LightGray.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
fun SensorStatusCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFFF1F5F9), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.CellTower, 
                        contentDescription = null, 
                        tint = Color(0xFF1E293B),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        "Sensor Status", 
                        fontWeight = FontWeight.ExtraBold, 
                        fontSize = 18.sp,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        "Network connectivity", 
                        color = Color(0xFF64748B), 
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            SensorItem("Water Quality Nodes", "Connected")
            Spacer(modifier = Modifier.height(12.dp))
            SensorItem("Weather Station", "Connected")
        }
    }
}

@Composable
fun SensorItem(name: String, status: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8FAFC)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                name, 
                fontSize = 15.sp, 
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1E293B)
            )
            Text(
                status, 
                color = FarmBrightGreen, 
                fontWeight = FontWeight.Bold, 
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ExpertSupportBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(20.dp)
    ) {
        Column {
            Text("Expert Support", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Need assistance with the recent livestock alert? Connect with local veterinary experts immediately.",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Text("Find Local Help", color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.weight(1f))
                FloatingActionButton(
                    onClick = {},
                    containerColor = Color.White.copy(alpha = 0.3f),
                    contentColor = Color.White,
                    modifier = Modifier.size(48.dp),
                    elevation = FloatingActionButtonDefaults.elevation(0.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    currentScreen: String, 
    onHistoryClick: () -> Unit = {},
    onReportsClick: () -> Unit = {},
    onDashboardClick: () -> Unit = {}
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentScreen == "Dashboard",
            onClick = onDashboardClick,
            icon = { Icon(Icons.Default.GridView, contentDescription = null) },
            label = { Text("Dashboard") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = FarmGrey,
                unselectedTextColor = FarmGrey,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
        NavigationBarItem(
            selected = currentScreen == "Reports",
            onClick = onReportsClick,
            icon = { Icon(Icons.Default.Assignment, contentDescription = null) },
            label = { Text("Reports") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = FarmGrey,
                unselectedTextColor = FarmGrey,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
        NavigationBarItem(
            selected = currentScreen == "History",
            onClick = onHistoryClick,
            icon = { Icon(Icons.Default.History, contentDescription = null) },
            label = { Text("History") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = FarmGrey,
                unselectedTextColor = FarmGrey,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
        NavigationBarItem(
            selected = currentScreen == "Alerts",
            onClick = {},
            icon = { Icon(Icons.Default.Notifications, contentDescription = null) },
            label = { Text("Alerts") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = FarmGrey,
                unselectedTextColor = FarmGrey,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    }
}
