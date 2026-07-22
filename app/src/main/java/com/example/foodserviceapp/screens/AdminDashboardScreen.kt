package com.example.foodserviceapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onEditClick: (String) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    var foodList by remember { mutableStateOf(listOf<AdminFoodItem>()) }
    var selectedTab by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        db.collection("foods").addSnapshotListener { result, error ->
            if (error != null || result == null) return@addSnapshotListener
            foodList = result.map { doc ->
                AdminFoodItem(
                    id = doc.id, foodName = doc.getString("foodName") ?: "",
                    quantity = doc.getString("quantity") ?: "", expiry = doc.getString("expiry") ?: "",
                    hotelName = doc.getString("hotelName") ?: "", status = doc.getString("status") ?: ""
                )
            }
        }
    }

    val available = foodList.filter { it.status == "available" }
    val claimed = foodList.filter { it.status == "claimed" }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Admin Console", fontWeight = FontWeight.Bold) }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().background(Color(0xFFF8F9FA))) {
            
            // Statistics Card
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF212529))
            ) {
                Row(modifier = Modifier.padding(20.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatItem("Total", "${foodList.size}")
                    StatItem("Claimed", "${claimed.size}")
                    StatItem("Impact", "High")
                }
            }

            TabRow(selectedTabIndex = selectedTab, containerColor = Color.White) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Active") })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("History") })
            }

            if (selectedTab == 0) {
                AdminList(available, onEdit = onEditClick, onDelete = { db.collection("foods").document(it).delete() })
            } else {
                AdminList(claimed, isHistory = true, onDelete = { db.collection("foods").document(it).delete() })
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color.Gray, fontSize = 12.sp)
        Text(value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun AdminList(list: List<AdminFoodItem>, isHistory: Boolean = false, onEdit: (String) -> Unit = {}, onDelete: (String) -> Unit) {
    if (list.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No data found", color = Color.Gray)
        }
    } else {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(list) { food ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(food.foodName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text("${food.quantity} • Exp: ${food.expiry}", color = Color.Gray)
                        }
                        if (!isHistory) {
                            IconButton(onClick = { onEdit(food.id) }) { Icon(Icons.Default.Edit, null, tint = Color.Blue) }
                        }
                        IconButton(onClick = { onDelete(food.id) }) { Icon(Icons.Default.Delete, null, tint = Color.Red) }
                    }
                }
            }
        }
    }
}
