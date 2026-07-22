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
    foodId: String? = null,
    onUploadClick: () -> Unit,
    onDashboardClick: () -> Unit
) {
    var foodName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }
    var hotelName by remember { mutableStateOf("") }
    var latitude by remember { mutableDoubleStateOf(0.0) }
    var longitude by remember { mutableDoubleStateOf(0.0) }
    
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(foodId) {
        if (foodId != null) {
            db.collection("foods").document(foodId).get().addOnSuccessListener { doc ->
                foodName = doc.getString("foodName") ?: ""
                quantity = doc.getString("quantity") ?: ""
                expiry = doc.getString("expiry") ?: ""
                hotelName = doc.getString("hotelName") ?: ""
                latitude = doc.getDouble("latitude") ?: 0.0
                longitude = doc.getDouble("longitude") ?: 0.0
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(20.dp).verticalScroll(rememberScrollState())) {
        Text(text = if (foodId == null) "Upload New Food" else "Edit Food Item", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        
        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(value = foodName, onValueChange = { foodName = it }, label = { Text("Food Name") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = quantity, onValueChange = { quantity = it }, label = { Text("Quantity") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = expiry, onValueChange = { expiry = it }, label = { Text("Expiry Time") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = hotelName, onValueChange = { hotelName = it }, label = { Text("Hotel Name") }, modifier = Modifier.fillMaxWidth())
                
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            LocationHelper.getCurrentLocation(context) { loc ->
                                if (loc != null) { latitude = loc.latitude; longitude = loc.longitude }
                            }
                        } else { permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE9ECEF), contentColor = Color.Black)
                ) {
                    Icon(Icons.Default.MyLocation, null)
                    Text(" Update GPS Location")
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            Button(
                onClick = {
                    isLoading = true
                    val data = hashMapOf(
                        "foodName" to foodName, "quantity" to quantity, "expiry" to expiry,
                        "hotelName" to hotelName, "latitude" to latitude, "longitude" to longitude,
                        "status" to "available", "updatedAt" to System.currentTimeMillis()
                    )
                    
                    val task = if (foodId == null) db.collection("foods").add(data) 
                               else db.collection("foods").document(foodId).set(data)
                    
                    task.addOnSuccessListener { onUploadClick() }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
            ) {
                Text(if (foodId == null) "Publish Food" else "Save Changes")
            }
        }
    }
}
