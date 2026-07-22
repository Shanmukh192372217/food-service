package com.example.foodserviceapp.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
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
import com.example.foodserviceapp.components.BottomBar

data class FoodItem(
    val id: String,
    val name: String,
    val hotel: String,
    val lat: Double,
    val lon: Double,
    val status: String,
    var distanceText: String = "Calculating..."
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onFoodClick: (String) -> Unit,
    onAlertClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    var foodList by remember { mutableStateOf(listOf<FoodItem>()) }
    var searchText by remember { mutableStateOf("") }
    val context = LocalContext.current
    
    var userLat by remember { mutableDoubleStateOf(0.0) }
    var userLon by remember { mutableDoubleStateOf(0.0) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            LocationHelper.getCurrentLocation(context) { location ->
                if (location != null) {
                    userLat = location.latitude
                    userLon = location.longitude
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationHelper.getCurrentLocation(context) { location ->
                if (location != null) {
                    userLat = location.latitude
                    userLon = location.longitude
                }
            }
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        db.collection("foods")
            .whereEqualTo("status", "available")
            .addSnapshotListener { result, error ->
                if (error != null || result == null) return@addSnapshotListener
                val items = result.map { doc ->
                    val fLat = doc.getDouble("latitude") ?: 0.0
                    val fLon = doc.getDouble("longitude") ?: 0.0
                    
                    val dist = if (userLat != 0.0 && fLat != 0.0) {
                        val d = LocationHelper.calculateDistance(userLat, userLon, fLat, fLon)
                        String.format("%.1f km", d)
                    } else {
                        "Near you"
                    }

                    FoodItem(
                        id = doc.id,
                        name = doc.getString("foodName") ?: "",
                        hotel = doc.getString("hotelName") ?: "",
                        lat = fLat,
                        lon = fLon,
                        status = doc.getString("status") ?: "",
                        distanceText = dist
                    )
                }
                foodList = items
            }
    }

    // Recalculate distances when user location changes
    LaunchedEffect(userLat, userLon) {
        if (userLat != 0.0) {
            foodList = foodList.map { item ->
                val d = LocationHelper.calculateDistance(userLat, userLon, item.lat, item.lon)
                item.copy(distanceText = String.format("%.1f km", d))
            }
        }
    }

    val filteredList = foodList.filter { it.name.contains(searchText, ignoreCase = true) }

    Scaffold(
        bottomBar = {
            BottomBar(onHomeClick = {}, onAlertClick = onAlertClick, onProfileClick = onProfileClick)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F9FA))
                .padding(16.dp)
        ) {
            Text(
                text = "Find Food Nearby",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212529)
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Search delicious food...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF5722),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (filteredList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No food available right now", color = Color.Gray)
                }
            } else {
                LazyColumn {
                    items(filteredList) { food ->
                        FoodCard(food = food, onClick = { onFoodClick(food.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun FoodCard(food: FoodItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = food.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Available",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color(0xFF2E7D32),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "${food.hotel} • ${food.distanceText}", color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
            ) {
                Text("View Details")
            }
        }
    }
}
