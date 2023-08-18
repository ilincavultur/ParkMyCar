package com.example.parkmycar.feature_map.data.remote.dto

import com.example.parkmycar.feature_map.domain.models.MarkerType
import com.example.parkmycar.feature_map.domain.models.Spot

data class PlaceDto(
    val formatted_address: String,
    val geometry: GeometryDto,
    val name: String,
    val opening_hours: OpeningHoursDto,
    val rating: Double
)

fun PlaceDto.toSpot() : Spot {
    return Spot(
        name = formatted_address,
        lat = geometry.location.lat,
        lng = geometry.location.lng,
        type = MarkerType.PARKING_SPOT,
        isSaved = false
    )
}
