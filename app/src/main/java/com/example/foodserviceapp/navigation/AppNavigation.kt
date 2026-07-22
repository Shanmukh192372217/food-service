package com.example.foodserviceapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.foodserviceapp.screens.*
@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        composable("splash") {

            SplashScreen(
                onSplashFinished = {
                    navController.navigate("login")
                }
            )
        }

        composable("login") {

            LoginScreen(

                onLoginClick = {
                    navController.navigate("home")
                },

                onAdminClick = {
                    navController.navigate("admin")
                }
            )
        }

        composable("home") {

            HomeScreen(
                onFoodClick = { foodId: String ->
                    navController.navigate("details/$foodId")
                },

                onAlertClick = {
                    navController.navigate("notifications")
                },

                onProfileClick = {
                    navController.navigate("profile")
                }
            )
        }

        composable("details/{foodId}") { backStackEntry ->

            val foodId = backStackEntry.arguments?.getString("foodId") ?: ""

            FoodDetailScreen(
                foodId = foodId,
                onClaimClick = { hotel, food ->
                    navController.navigate("claim/$hotel/$food")
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("claim/{hotel}/{food}") { backStackEntry ->
            val hotel = backStackEntry.arguments?.getString("hotel") ?: ""
            val food = backStackEntry.arguments?.getString("food") ?: ""

            ClaimSuccessScreen(
                hotelName = hotel,
                foodName = food,
                onMapClick = {
                    navController.navigate("map/$hotel")
                },
                onHomeClick = {
                    navController.navigate("home")
                }
            )
        }

        composable("admin") {

            AdminUploadScreen(
                onUploadClick = {
                    navController.navigate("uploadsuccess")
                },
                onDashboardClick = {
                    navController.navigate("admindashboard")
                }
            )
        }

        composable("admindashboard") {
            AdminDashboardScreen()
        }

        composable("notifications") {

            NotificationScreen(

                onHomeClick = {
                    navController.navigate("home")
                },

                onProfileClick = {
                    navController.navigate("profile")
                }
            )
        }

        composable("profile") {

            ProfileScreen(

                onHomeClick = {
                    navController.navigate("home")
                },

                onAlertClick = {
                    navController.navigate("notifications")
                },

                onLogoutClick = {
                    navController.navigate("login")
                }
            )
        }
        composable("map/{hotel}") { backStackEntry ->
            val hotel = backStackEntry.arguments?.getString("hotel") ?: ""
            MapScreen(hotelName = hotel)
        }
        composable("uploadsuccess") {
            UploadSuccessScreen(
                onUploadMoreClick = {
                    navController.navigate("admin")
                }
            )
        }
    }
}