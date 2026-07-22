package com.example.foodserviceapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.foodserviceapp.R
@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onAdminClick: () -> Unit
){
    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFF9800),
                        Color(0xFFFF5722)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(20.dp)
        ) {

            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.food_service_logo),
                    contentDescription = "Food Service Logo",
                    modifier = Modifier.size(110.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Food Service",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Reduce Food Waste",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    label = {
                        Text("Email")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    label = {
                        Text("Password")
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener {

                                if (email == "hoteladmin@gmail.com") {
                                    onAdminClick()
                                } else {
                                    onLoginClick()
                                }
                            }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("Login")
                    Spacer(modifier = Modifier.height(16.dp))


                }
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener {
                                errorMessage = "Invalid email or password"
                                onLoginClick()
                            }
                    }
                ) {
                    Text("Create New Account")
                }
            }
        }
    }
}