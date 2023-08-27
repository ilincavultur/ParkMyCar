package com.example.parkmycar

import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.parkmycar.core.components.AppScaffold
import com.example.parkmycar.core.navigation.Navigation

import com.example.parkmycar.ui.theme.ParkMyCarTheme
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint

lateinit var locationCallback: LocationCallback
lateinit var fusedLocationClient: FusedLocationProviderClient
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //installSplashScreen()

        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setContent {
            ParkMyCarTheme {
                AppScaffold()
            }
        }
    }

}



