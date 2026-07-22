package com.example.foodserviceapp.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.ui.Modifier

@Composable
fun BottomBar(
    onHomeClick: () -> Unit,
    onAlertClick: () -> Unit,
    onProfileClick: () -> Unit
) {

    NavigationBar(
        modifier = Modifier.navigationBarsPadding()
    ) {
        NavigationBarItem(
            selected = true,
            onClick = {
                onHomeClick()
            },
            icon = {
                Icon(Icons.Default.Home, contentDescription = "Home")
            },
            label = {
                Text("Home")
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                onAlertClick()
            },
            icon = {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications")
            },
            label = {
                Text("Alerts")
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                onProfileClick()
            },
            icon = {
                Icon(Icons.Default.Person, contentDescription = "Profile")
            },
            label = {
                Text("Profile")
            }
        )
    }
}