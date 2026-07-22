package com.example.foodserviceapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailScreen(
    foodId: String,
    onClaimClick: (String, String) -> Unit,
    onBackClick: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    var foodName by remember { mutableStateOf("Loading...") }
    var hotelName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var distance by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }

    LaunchedEffect(foodId) {
        db.collection("foods")
            .document(foodId)
            .get()
            .addOnSuccessListener { document ->
                foodName = document.getString("foodName") ?: ""
                hotelName = document.getString("hotelName") ?: ""
                quantity = document.getString("quantity") ?: ""
                distance = document.getString("distance") ?: ""
                expiry = document.getString("expiry") ?: ""
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Food Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F9FA))
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFE9ECEF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Restaurant,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = foodName,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212529)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFFF5722), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$hotelName • $distance",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Divider()

            Spacer(modifier = Modifier.height(24.dp))

            DetailItem(icon = Icons.Default.Restaurant, label = "Quantity", value = quantity)
            Spacer(modifier = Modifier.height(16.dp))
            DetailItem(icon = Icons.Default.Timer, label = "Expires In", value = expiry, color = Color(0xFFD32F2F))

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val userEmail = auth.currentUser?.email ?: "Guest"

                    db.collection("foods")
                        .document(foodId)
                        .update("status", "claimed")
                        .addOnSuccessListener {
                            val claimedFood = hashMapOf(
                                "foodId" to foodId,
                                "user" to userEmail,
                                "foodName" to foodName,
                                "hotelName" to hotelName,
                                "claimedAt" to System.currentTimeMillis()
                            )

                            db.collection("claimedFoods")
                                .add(claimedFood)
                                .addOnSuccessListener {
                                    val notificationData = hashMapOf(
                                        "title" to "Success!",
                                        "message" to "You successfully claimed $foodName from $hotelName.",
                                        "time" to System.currentTimeMillis()
                                    )
                                    db.collection("notifications").add(notificationData)
                                    onClaimClick(hotelName, foodName)
                                }
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
            ) {
                Text("Claim Food", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun DetailItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, color: Color = Color.Black) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp), tint = Color.Gray)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, fontSize = 14.sp, color = Color.Gray)
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Medium, color = color)
        }
    }
}
