package com.example.parkmycar.feature_map.data.remote.dto.directions_api

data class GeocodedWaypointDto(
    val geocoder_status: String,
    val place_id: String,
    val types: List<String>
)