package com.example.parkmycar.feature_map.data.remote.dto.directions_api

data class StepDto(
    val distance: DistanceDto,
    val duration: DurationDto,
    val end_location: EndLocationDto,
    val html_instructions: String,
    val maneuver: String,
    val polyline: PolylineDto,
    val start_location: StartLocationDto,
    val travel_mode: String
)