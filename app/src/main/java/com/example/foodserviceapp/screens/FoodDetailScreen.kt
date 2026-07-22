package com.example.foodserviceapp.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current

    var foodName by remember { mutableStateOf("Loading...") }
    var hotelName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var distance by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var rating by remember { mutableFloatStateOf(0f) }
    
    var showRatingDialog by remember { mutableStateOf(false) }

    LaunchedEffect(foodId) {
        db.collection("foods").document(foodId).get().addOnSuccessListener { document ->
            foodName = document.getString("foodName") ?: ""
            hotelName = document.getString("hotelName") ?: ""
            quantity = document.getString("quantity") ?: ""
            distance = "${document.getDouble("latitude") ?: 0.0}"
            expiry = document.getString("expiry") ?: ""
            status = document.getString("status") ?: ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Food Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).background(Color(0xFFF8F9FA)).padding(16.dp).verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().height(250.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE9ECEF))
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Restaurant, null, modifier = Modifier.size(100.dp), tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = foodName, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Text(text = "$hotelName", fontSize = 20.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            DetailRow(Icons.Default.Scale, "Quantity", quantity)
            DetailRow(Icons.Default.Timer, "Available Until", expiry)
            DetailRow(Icons.Default.Star, "User Rating", if (rating == 0f) "No ratings yet" else "$rating / 5.0")

            Spacer(modifier = Modifier.weight(1f))

            if (status == "available") {
                Button(
                    onClick = {
                        val userEmail = auth.currentUser?.email ?: "Guest"
                        db.collection("foods").document(foodId).update("status", "claimed")
                            .addOnSuccessListener {
                                val claimedData = hashMapOf(
                                    "foodId" to foodId, "user" to userEmail, "foodName" to foodName,
                                    "hotelName" to hotelName, "claimedAt" to System.currentTimeMillis()
                                )
                                db.collection("claimedFoods").add(claimedData)
                                onClaimClick(hotelName, foodName)
                            }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
                ) {
                    Text("Claim This Food", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = { showRatingDialog = true },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Rate Experience", fontSize = 18.sp)
                }
            }
        }
    }

    if (showRatingDialog) {
        AlertDialog(
            onDismissRequest = { showRatingDialog = false },
            title = { Text("Rate Food Quality") },
            text = {
                Column {
                    Text("How was the food from $hotelName?")
                    Slider(value = rating, onValueChange = { rating = it }, valueRange = 0f..5f, steps = 4)
                    Text("Rating: ${rating.toInt()} Stars", modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            },
            confirmButton = {
                TextButton(onClick = { 
                    Toast.makeText(context, "Thank you for feedback!", Toast.LENGTH_SHORT).show()
                    showRatingDialog = false 
                }) { Text("Submit") }
            }
        )
    }
}

@Composable
fun DetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = Color.Gray)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 12.sp, color = Color.Gray)
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Medium)
        }
    }
}
