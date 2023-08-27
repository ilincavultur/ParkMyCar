package com.example.parkmycar.core.navigation

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.parkmycar.core.components.tabs.TabLayout
import com.example.parkmycar.feature_map.presentation.MapScreen
import com.example.parkmycar.feature_saved_parking_lots.presentation.SavedParkingSpotsScreen
import com.example.parkmycar.feature_saved_spots.presentation.SavedCarSpotsScreen
import com.example.parkmycar.feature_splash_screen.presentation.SplashScreen

@SuppressLint("MissingPermission")
@Composable
fun Navigation(
    navController: NavHostController,
    innerPadding: PaddingValues,
    snackbarHostState: SnackbarHostState
) {
   NavHost(navController = navController, startDestination = Screen.SplashScreen.route, Modifier.padding(innerPadding)) {
       composable(route = Screen.SplashScreen.route) {
           SplashScreen(navController = navController)
       }

       composable(route = Screen.MapScreen.route) {
           MapScreen(navController = navController, snackbarHostState = snackbarHostState)
       }

       composable(route = Screen.SavedCarSpotsScreen.route) {
           SavedCarSpotsScreen(navController = navController)
       }

       composable(route = Screen.SavedParkingSpotsScreen.route) {
           SavedParkingSpotsScreen(navController = navController)
       }

       composable(route = Screen.TabLayout.route) {
           TabLayout(navController = navController)
       }
   }
}