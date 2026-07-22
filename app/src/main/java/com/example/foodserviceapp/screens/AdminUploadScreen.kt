package com.example.foodserviceapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AdminUploadScreen(
    onUploadClick: () -> Unit,
    onDashboardClick: () -> Unit
) {
    var foodName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }
    var hotelName by remember { mutableStateOf("") }
    var distance by remember { mutableStateOf("") }

    val db = FirebaseFirestore.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(
            text = "Upload Available Food",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = foodName,
            onValueChange = { foodName = it },
            label = { Text("Food Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("Quantity") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = expiry,
            onValueChange = { expiry = it },
            label = { Text("Available Till") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = hotelName,
            onValueChange = { hotelName = it },
            label = { Text("Hotel Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = distance,
            onValueChange = { distance = it },
            label = { Text("Distance") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val foodData = hashMapOf(
                    "foodName" to foodName,
                    "quantity" to quantity,
                    "expiry" to expiry,
                    "hotelName" to hotelName,
                    "distance" to distance,
                    "status" to "available",
                    "createdAt" to System.currentTimeMillis()
                )

                db.collection("foods")
                    .add(foodData)
                    .addOnSuccessListener {

                        val notificationData = hashMapOf(
                            "title" to "New Food Uploaded",
                            "message" to "$foodName available at $hotelName",
                            "time" to System.currentTimeMillis()
                        )

                        db.collection("notifications")
                            .add(notificationData)

                        onUploadClick()
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Upload Food",
                fontSize = 18.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onDashboardClick()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "View Dashboard",
                fontSize = 18.sp
            )
        }
    }
}