package com.example.parkmycar.feature_map.presentation

sealed class MapUiEvent {
    data class ShowSnackbar(val message: String) : MapUiEvent()
    data class LaunchPermissionLauncher(val message: String): MapUiEvent()
}