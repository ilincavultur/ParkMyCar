package com.example.parkmycar.feature_map.domain.models

data class Spot(
    val id: Int? = null,
    val lat: Double,
    val lng: Double,
    val type: MarkerType,
    val isSaved: Boolean? = false
)