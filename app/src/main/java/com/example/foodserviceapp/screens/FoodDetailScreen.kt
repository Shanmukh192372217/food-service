package com.example.foodserviceapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun FoodDetailScreen(
    foodId: String,
    onClaimClick: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    var foodName by remember { mutableStateOf("") }
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Food Image",
                    fontSize = 24.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = foodName,
            fontSize = 32.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Hotel: $hotelName",
            fontSize = 20.sp
        )

        Text(
            text = "Quantity: $quantity",
            fontSize = 20.sp
        )

        Text(
            text = "Distance: $distance",
            fontSize = 20.sp
        )

        Text(
            text = "Available Till: $expiry",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val userEmail = auth.currentUser?.email ?: "Unknown"

                db.collection("foods")
                    .document(foodId)
                    .update("status", "claimed")
                    .addOnSuccessListener {
                        val claimedFood = hashMapOf(
                            "foodId" to foodId,
                            "user" to userEmail,
                            "foodName" to foodName,
                            "hotelName" to hotelName,
                            "time" to System.currentTimeMillis()
                        )

                        db.collection("claimedFoods")
                            .add(claimedFood)
                            .addOnSuccessListener {

                                val notificationData = hashMapOf(
                                    "title" to "Food Claimed",
                                    "message" to "$foodName was claimed successfully",
                                    "time" to System.currentTimeMillis()
                                )

                                db.collection("notifications")
                                    .add(notificationData)

                                onClaimClick()
                            }
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Claim Food",
                fontSize = 18.sp
            )
        }
    }
}