package com.example.foodserviceapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodserviceapp.components.BottomBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class ClaimedFoodHistory(
    val name: String,
    val hotel: String,
    val date: String
)

@Composable
fun ProfileScreen(
    onHomeClick: () -> Unit,
    onAlertClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val userEmail = auth.currentUser?.email ?: "User"

    var claimedHistory by remember {
        mutableStateOf(listOf<ClaimedFoodHistory>())
    }

    LaunchedEffect(Unit) {
        db.collection("claimedFoods")
            .whereEqualTo("user", userEmail)
            .addSnapshotListener { result, error ->
                if (error != null || result == null) return@addSnapshotListener
                
                val history = result.map { doc ->
                    ClaimedFoodHistory(
                        name = doc.getString("foodName") ?: "Unknown Food",
                        hotel = doc.getString("hotelName") ?: "Unknown Hotel",
                        date = "Recently Claimed"
                    )
                }
                claimedHistory = history
            }
    }

    Scaffold(
        bottomBar = {
            BottomBar(
                onHomeClick = onHomeClick,
                onAlertClick = onAlertClick,
                onProfileClick = { }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F9FA))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = Color(0xFFFF5722).copy(alpha = 0.1f)
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.padding(20.dp),
                    tint = Color(0xFFFF5722)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = userEmail, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = "Food Hero", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.History, contentDescription = null, tint = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Claim History", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(claimedHistory) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = item.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(text = item.hotel, fontSize = 14.sp, color = Color.Gray)
                            }
                            Text(text = "Success", color = Color(0xFF4CAF50), fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    auth.signOut()
                    onLogoutClick()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Logout", color = Color.Black)
            }
        }
    }
}
