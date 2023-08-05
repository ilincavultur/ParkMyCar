package com.example.parkmycar.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.parkmycar.feature_splash_screen.presentation.SplashScreen

@Composable
fun Navigation(
    navController: NavHostController
) {
   NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
       composable(route = Screen.SplashScreen.route) {
           SplashScreen(navController = navController)
       }

       composable(route = Screen.HomeScreen.route) {

       }
   }
}