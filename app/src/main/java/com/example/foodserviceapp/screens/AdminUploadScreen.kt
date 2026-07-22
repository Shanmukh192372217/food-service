package com.example.foodserviceapp.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Upload Available Food",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF5722)
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = foodName,
            onValueChange = { foodName = it },
            label = { Text("Food Name (e.g. Biryani)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("Quantity (e.g. 5 Plates)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = expiry,
            onValueChange = { expiry = it },
            label = { Text("Available Till (e.g. 10:00 PM)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = hotelName,
            onValueChange = { hotelName = it },
            label = { Text("Hotel Name") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = distance,
            onValueChange = { distance = it },
            label = { Text("Distance (e.g. 2.5 km)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color(0xFFFF5722)
            )
        } else {
            Button(
                onClick = {
                    if (foodName.isEmpty() || quantity.isEmpty() || hotelName.isEmpty()) {
                        Toast.makeText(context, "Please fill main fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isLoading = true
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
                                "title" to "New Food Added!",
                                "message" to "$foodName is now available at $hotelName",
                                "time" to System.currentTimeMillis()
                            )

                            db.collection("notifications")
                                .add(notificationData)
                                .addOnCompleteListener {
                                    isLoading = false
                                    onUploadClick()
                                }
                        }
                        .addOnFailureListener { e ->
                            isLoading = false
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
            ) {
                Text(text = "Upload Food", fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { onDashboardClick() },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = !isLoading
        ) {
            Text(text = "View Dashboard", fontSize = 18.sp)
        }
    }
}
