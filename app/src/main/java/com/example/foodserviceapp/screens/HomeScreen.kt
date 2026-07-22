package com.example.foodserviceapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.*
import com.google.firebase.firestore.FirebaseFirestore
import com.example.foodserviceapp.components.BottomBar
import com.example.foodserviceapp.components.TopBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class FoodItem(
    val id: String,
    val name: String,
    val hotel: String,
    val distance: String
)

@Composable
fun HomeScreen(
    onFoodClick: (String) -> Unit,
    onAlertClick: () -> Unit,
    onProfileClick: () -> Unit
) {

    val db = FirebaseFirestore.getInstance()

    var foodList by remember {
        mutableStateOf(listOf<FoodItem>())
    }
    var searchText by remember {
        mutableStateOf("")
    }
    LaunchedEffect(Unit) {

        db.collection("foods")
            .addSnapshotListener { result, error ->

                if (error != null || result == null) {
                    return@addSnapshotListener
                }

                val items = mutableListOf<FoodItem>()

                for (document in result) {

                    val status = document.getString("status") ?: ""

                    if (status == "available") {

                        items.add(
                            FoodItem(
                                id = document.id,
                                name = document.getString("foodName") ?: "",
                                hotel = document.getString("hotelName") ?: "",
                                distance = document.getString("distance") ?: ""
                            )
                        )
                    }
                }
                foodList = items
            }
    }
    val filteredFoodList = foodList.filter {
        it.name.contains(searchText, ignoreCase = true)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {

        Text(
            text = "Nearby Available Food",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
            },
            label = {
                Text("Search Food")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Spacer(modifier = Modifier.height(20.dp))
        if (foodList.isEmpty()) {
            Text(
                text = "No Food Available Right Now",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Please check again later."
            )
        } else {

        }
        LazyColumn {
            items(filteredFoodList) { food ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = food.name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFF3CD)
                            )
                        ) {
                            Text(
                                text = "⏰ Expires Soon",
                                modifier = Modifier.padding(8.dp),
                                color = Color(0xFF856404)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Hotel: ${food.hotel}"
                        )

                        Text(
                            text = "Distance: ${food.distance}"
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                onFoodClick(food.id)
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {

                            Text("View")
                        }
                    }
                }
            }
        }
    }
    BottomBar(
        onHomeClick = { },

        onAlertClick = {
            onAlertClick()
        },

        onProfileClick = {
            onProfileClick()
        }
    )
}