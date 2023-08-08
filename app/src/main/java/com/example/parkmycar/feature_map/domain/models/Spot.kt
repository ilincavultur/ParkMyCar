package com.example.parkmycar.feature_map.domain.models

data class Spot(
    val lat: Double,
    val lng: Double,
    val type: MarkerType
)