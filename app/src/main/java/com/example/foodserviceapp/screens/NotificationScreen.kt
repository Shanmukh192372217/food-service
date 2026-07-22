package com.example.foodserviceapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodserviceapp.components.BottomBar
import com.google.firebase.firestore.FirebaseFirestore

data class NotificationItem(
    val title: String,
    val message: String
)

@Composable
fun NotificationScreen(
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()

    var notifications by remember {
        mutableStateOf(listOf<NotificationItem>())
    }

    LaunchedEffect(Unit) {
        db.collection("notifications")
            .addSnapshotListener { result, error ->

                if (error != null || result == null) {
                    return@addSnapshotListener
                }

                val items = mutableListOf<NotificationItem>()

                for (document in result) {
                    items.add(
                        NotificationItem(
                            title = document.getString("title") ?: "",
                            message = document.getString("message") ?: ""
                        )
                    )
                }

                notifications = items
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        Text(
            text = "Notifications",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(notifications) { item ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = item.title,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = item.message,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }

        BottomBar(
            onHomeClick = {
                onHomeClick()
            },
            onAlertClick = { },
            onProfileClick = {
                onProfileClick()
            }
        )
    }
}