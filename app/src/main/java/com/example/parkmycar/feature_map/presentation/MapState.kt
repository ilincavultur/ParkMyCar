package com.example.parkmycar.feature_map.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import com.example.parkmycar.feature_map.domain.models.Spot

data class MapState(
    var isLoading: Boolean = false,
    val spots: List<Spot> = emptyList(),
    var isMapLoaded: Boolean = false,
    var permissionsGranted: Boolean = false,
    var visiblePermissionDialogQueue: MutableList<String> = mutableStateListOf<String>()
)