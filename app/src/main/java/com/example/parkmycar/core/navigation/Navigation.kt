package com.example.parkmycar.core.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.parkmycar.feature_map.presentation.MapScreen
import com.example.parkmycar.feature_splash_screen.presentation.SplashScreen

@Composable
fun Navigation(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
   NavHost(navController = navController, startDestination = Screen.SplashScreen.route, Modifier.padding(innerPadding)) {
       composable(route = Screen.SplashScreen.route) {
           SplashScreen(navController = navController)
       }

       composable(route = Screen.MapScreen.route) {
           MapScreen(navController = navController)
       }

       composable(route = Screen.SavedCarSpotsScreen.route) {
           //SplashScreen(navController = navController)
       }

       composable(route = Screen.SavedParkingSpotsScreen.route) {
           //SplashScreen(navController = navController)
       }
   }
}