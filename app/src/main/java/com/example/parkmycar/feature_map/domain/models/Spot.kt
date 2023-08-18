package com.example.parkmycar.feature_map.domain.models

data class Spot(
    val id: Int? = null,
    val name: String? = "",
    val lat: Double? = 0.0,
    val lng: Double? = 0.0,
    val type: MarkerType? = MarkerType.CAR_SPOT,
    val isSaved: Boolean? = false
)