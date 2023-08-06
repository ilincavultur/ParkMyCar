package com.example.parkmycar.core.components.tabs

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.parkmycar.core.navigation.Screen
import com.example.parkmycar.feature_saved_parking_lots.presentation.SavedParkingSpotsScreen
import com.example.parkmycar.feature_saved_spots.presentation.SavedCarSpotsScreen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabsContent(
    pagerState: PagerState,
    navController: NavController
) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> SavedCarSpotsScreen(navController = navController)
            1 -> SavedParkingSpotsScreen(navController = navController)
        }
    }
}