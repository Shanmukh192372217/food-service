package com.example.foodserviceapp.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.foodserviceapp.utils.LocationHelper
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
    
    var latitude by remember { mutableDoubleStateOf(0.0) }
    var longitude by remember { mutableDoubleStateOf(0.0) }
    var locationText by remember { mutableStateOf("Location not set") }
    
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            LocationHelper.getCurrentLocation(context) { location ->
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                    locationText = "Location Set: ${String.format("%.4f", latitude)}, ${String.format("%.4f", longitude)}"
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Upload New Food Item",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212529)
        )
        Text(text = "Share food with coordinates for accuracy", color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = foodName,
                    onValueChange = { foodName = it },
                    label = { Text("Food Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = expiry,
                    onValueChange = { expiry = it },
                    label = { Text("Available Till") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = hotelName,
                    onValueChange = { hotelName = it },
                    label = { Text("Hotel Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Location Fetcher
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                LocationHelper.getCurrentLocation(context) { location ->
                                    if (location != null) {
                                        latitude = location.latitude
                                        longitude = location.longitude
                                        locationText = "Location Set: ${String.format("%.4f", latitude)}, ${String.format("%.4f", longitude)}"
                                    }
                                }
                            } else {
                                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE9ECEF), contentColor = Color.Black)
                    ) {
                        Icon(Icons.Default.MyLocation, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Get My Location")
                    }
                }
                Text(text = locationText, fontSize = 12.sp, color = if(latitude != 0.0) Color(0xFF2E7D32) else Color.Red, modifier = Modifier.padding(top = 4.dp))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally), color = Color(0xFFFF5722))
        } else {
            Button(
                onClick = {
                    if (foodName.isEmpty() || quantity.isEmpty() || hotelName.isEmpty() || latitude == 0.0) {
                        Toast.makeText(context, "Please fill all fields and set location", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    isLoading = true
                    
                    val foodData = hashMapOf(
                        "foodName" to foodName,
                        "quantity" to quantity,
                        "expiry" to expiry,
                        "hotelName" to hotelName,
                        "latitude" to latitude,
                        "longitude" to longitude,
                        "status" to "available",
                        "createdAt" to System.currentTimeMillis()
                    )

                    db.collection("foods")
                        .add(foodData)
                        .addOnSuccessListener {
                            isLoading = false
                            onUploadClick()
                        }
                        .addOnFailureListener {
                            isLoading = false
                            Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT).show()
                        }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
            ) {
                Text(text = "Publish Food", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onDashboardClick,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Back to Dashboard", color = Color.Gray)
        }
    }
}
