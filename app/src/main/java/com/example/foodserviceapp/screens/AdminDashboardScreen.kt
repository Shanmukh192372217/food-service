package com.example.foodserviceapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore

data class AdminFoodItem(
    val id: String,
    val foodName: String,
    val quantity: String,
    val expiry: String,
    val hotelName: String,
    val status: String
)

@Composable
fun AdminDashboardScreen() {
    val db = FirebaseFirestore.getInstance()
    var foodList by remember { mutableStateOf(listOf<AdminFoodItem>()) }
    
    // ... rest of state
    val totalFoods = foodList.size
    val availableFoods = foodList.count { it.status == "available" }
    val claimedFoods = foodList.count { it.status == "claimed" }
    LaunchedEffect(Unit) {
        db.collection("foods")
            .addSnapshotListener { result, error ->

                if (error != null || result == null) {
                    return@addSnapshotListener
                }

                val items = mutableListOf<AdminFoodItem>()

                for (document in result) {
                    items.add(
                        AdminFoodItem(
                            id = document.id,
                            foodName = document.getString("foodName") ?: "",
                            quantity = document.getString("quantity") ?: "",
                            expiry = document.getString("expiry") ?: "",
                            hotelName = document.getString("hotelName") ?: "",
                            status = document.getString("status") ?: ""
                        )
                    )
                }

                foodList = items
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {

        Text(
            text = "Admin Dashboard",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Total")
                    Text("$totalFoods", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Available")
                    Text("$availableFoods", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Claimed")
                    Text("$claimedFoods", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Uploaded Foods",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))
        if (foodList.isEmpty()) {

            Text(
                text = "No Foods Uploaded Yet",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Upload food to see data here."
            )

        } else {
        }
        LazyColumn {

            items(foodList) { food ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {

                        Text(
                            text = food.foodName,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "Hotel: ${food.hotelName}")
                        Text(text = "Quantity: ${food.quantity}")
                        Text(text = "Available Till: ${food.expiry}")
                        Text(text = "Status: ${food.status}")

                        Spacer(modifier = Modifier.height(12.dp))

                        IconButton(
                            onClick = {
                                db.collection("foods").document(food.id).delete()
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}