package com.example.parkmycar.core.navigation

import androidx.annotation.StringRes
import com.example.parkmycar.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object SplashScreen : Screen("splash_screen", resourceId = R.string.splash_screen)
    object MapScreen : Screen("map_screen", resourceId = R.string.map_screen)
    object SavedCarSpotsScreen : Screen("saved_car_spots_screen", resourceId = R.string.saved_car_spots_screen)
    object SavedParkingSpotsScreen : Screen("saved_parking_spots_screen", resourceId = R.string.saved_parking_spots_screen)
    object TabLayout : Screen("tab_layout", R.string.tab_layout)
}
