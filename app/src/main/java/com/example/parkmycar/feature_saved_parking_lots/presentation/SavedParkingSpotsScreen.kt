package com.example.parkmycar.feature_saved_parking_lots.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun SavedParkingSpotsScreen(
    navController: NavController
) {
    Surface(modifier = Modifier.fillMaxSize().background(color = Color.Red)) {

    }
    Column {
        Text(text = "SavedParkingSpotsScreen", color = Color.Black)
    }


}