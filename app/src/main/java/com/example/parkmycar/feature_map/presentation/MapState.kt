package com.example.parkmycar.feature_map.presentation

import com.example.parkmycar.feature_map.domain.models.Spot

data class MapState(
    var isLoading: Boolean = false,
    val spots: List<Spot> = emptyList()
)