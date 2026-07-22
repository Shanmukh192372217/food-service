package com.example.foodserviceapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UploadSuccessScreen(
    onUploadMoreClick: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Food Uploaded Successfully!",
                fontSize = 28.sp
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Nearby users will receive notifications."
            )
            Spacer(
                modifier = Modifier.height(30.dp)
            )

            Button(
                onClick = {
                    onUploadMoreClick()
                }
            ) {
                Text("Upload More")
            }
        }
    }
}