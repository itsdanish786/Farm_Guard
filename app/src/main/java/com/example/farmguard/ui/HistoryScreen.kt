package com.example.farmguard.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.farmguard.ui.theme.*

data class HistoryItem(
    val title: String,
    val date: String,
    val time: String,
    val status: String,
    val statusColor: Color,
    val icon: ImageVector,
    val iconBg: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBackClick: () -> Unit = {},
    onDashboardClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onReportsClick: () -> Unit = {}
) {
    val historyItems = listOf(
        HistoryItem("North Field Corn", "Oct 24, 2023", "09:15 AM", "Diagnosis Ready", FarmBrightGreen, Icons.Default.Eco, Color(0xFFDCFCE7)),
        HistoryItem("East Block Irrigation", "Oct 20, 2023", "14:30 PM", "Archived", FarmGrey, Icons.Default.WaterDrop, Color(0xFFE0F2FE)),
        HistoryItem("South Orchard Pests", "Oct 18, 2023", "08:45 AM", "Attention Required", FarmAlertRed, Icons.Default.BugReport, Color(0xFFFEF2F2)),
        HistoryItem("West Field Soybeans", "Oct 15, 2023", "11:20 AM", "Archived", FarmGrey, Icons.Default.Eco, Color(0xFFDCFCE7))
    )

    Scaffold(
        bottomBar = { 
            BottomNavigationBar(
                currentScreen = "History", 
                onDashboardClick = onDashboardClick,
                onReportsClick = onReportsClick
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
                        text = "History",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Review your past field reports and crop diagnostics.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = FarmGrey
                    )
                }
            }

            item {
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, Color.Transparent)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = FarmGrey)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Search reports...", color = FarmGrey, modifier = Modifier.weight(1f))
                        Icon(Icons.Default.Tune, contentDescription = null, tint = FarmGrey)
                    }
                }
            }

            items(historyItems) { item ->
                HistoryCard(item)
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun HistoryCard(item: HistoryItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (ThemeState.isDarkMode) item.iconBg.copy(alpha = 0.2f) else item.iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(item.icon, contentDescription = null, tint = item.statusColor)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(item.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = item.statusColor.copy(alpha = 0.1f)
                    ) {
                        Text(
                            item.status,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            color = item.statusColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(14.dp), tint = FarmGrey)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${item.date} • ${item.time}", color = FarmGrey, fontSize = 12.sp)
                }
            }
        }
    }
}
