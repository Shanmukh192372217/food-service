package com.example.foodserviceapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ClaimSuccessScreen(
    onMapClick: () -> Unit,
    onHomeClick: () -> Unit
){

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {

            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Food Claimed Successfully!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Hotel: Paradise Hotel",
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Food: Chicken Biryani"
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Pickup Before: 11:00 PM"
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        onMapClick()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("Open Maps")
                }
                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Button(
                    onClick = {
                        onHomeClick()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Go Home")
                }
            }
        }
    }
}