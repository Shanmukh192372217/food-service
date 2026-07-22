package com.example.foodserviceapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodserviceapp.components.BottomBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.foundation.layout.statusBarsPadding

@Composable
fun ProfileScreen(
    onHomeClick: () -> Unit,
    onAlertClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val userEmail = auth.currentUser?.email ?: "User"

    var claimedFoods by remember {
        mutableStateOf(listOf<String>())
    }

    LaunchedEffect(Unit) {
        db.collection("claimedFoods")
            .whereEqualTo("user", userEmail)
            .addSnapshotListener { result, error ->

                if (error != null || result == null) {
                    return@addSnapshotListener
                }
                val foods = mutableListOf<String>()

                for (document in result) {
                    foods.add(document.getString("foodName") ?: "Claimed Food")
                }

                claimedFoods = foods
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier.size(120.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "User", fontSize = 24.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Profile", fontSize = 30.sp)
        Text(text = userEmail, fontSize = 18.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "Claimed Foods", fontSize = 24.sp)

                Spacer(modifier = Modifier.height(16.dp))

                if (claimedFoods.isEmpty()) {
                    Text(text = "No claimed foods yet")
                } else {
                    claimedFoods.forEach { food ->
                        Text(text = "• $food")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                auth.signOut()
                onLogoutClick()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Logout", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        BottomBar(
            onHomeClick = onHomeClick,
            onAlertClick = onAlertClick,
            onProfileClick = { }
        )
    }
}