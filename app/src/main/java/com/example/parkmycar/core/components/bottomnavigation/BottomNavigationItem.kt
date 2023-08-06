package com.example.parkmycar.core.components.bottomnavigation

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val route: String,
    val icon: ImageVector,
    val resourceId: Int
)
