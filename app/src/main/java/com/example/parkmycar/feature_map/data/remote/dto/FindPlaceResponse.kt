package com.example.parkmycar.feature_map.data.remote.dto

import com.example.parkmycar.feature_map.domain.models.MarkerType
import com.example.parkmycar.feature_map.domain.models.Spot

data class FindPlaceResponse(
    val candidates: List<PlaceDto>,
    val status: String
)

fun FindPlaceResponse.toSpotList() : List<Spot> {
    return candidates.map {
        Spot(
            lat = it.geometry.location.lat,
            lng = it.geometry.location.lng,
            type = MarkerType.PARKING_SPOT
        )
    }
}