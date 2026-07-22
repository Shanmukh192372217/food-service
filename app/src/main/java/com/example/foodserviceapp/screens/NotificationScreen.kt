package com.example.foodserviceapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodserviceapp.components.BottomBar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

data class NotificationItem(
    val title: String,
    val message: String,
    val time: Long = 0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    var notifications by remember { mutableStateOf(listOf<NotificationItem>()) }

    LaunchedEffect(Unit) {
        db.collection("notifications")
            .orderBy("time", Query.Direction.DESCENDING)
            .addSnapshotListener { result, error ->
                if (error != null || result == null) return@addSnapshotListener
                notifications = result.map { doc ->
                    NotificationItem(
                        title = doc.getString("title") ?: "",
                        message = doc.getString("message") ?: "",
                        time = doc.getLong("time") ?: 0
                    )
                }
            }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Alerts", fontWeight = FontWeight.Bold) }) },
        bottomBar = { BottomBar(onHomeClick = onHomeClick, onAlertClick = {}, onProfileClick = onProfileClick) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().background(Color(0xFFF8F9FA)).padding(16.dp)) {
            if (notifications.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.NotificationsNone, null, modifier = Modifier.size(80.dp), tint = Color.LightGray)
                        Text("All caught up!", color = Color.Gray)
                    }
                }
            } else {
                LazyColumn {
                    items(notifications) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(item.title, fontWeight = FontWeight.Bold, color = Color(0xFFFF5722))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(item.message)
                            }
                        }
                    }
                }
            }
        }
    }
}
