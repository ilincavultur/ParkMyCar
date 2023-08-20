package com.example.parkmycar.feature_map.data.remote.dto.directions_api

data class LegDto(
    val distance: DistanceDto,
    val duration: DurationDto,
    val end_address: String,
    val end_location: EndLocationDto,
    val start_address: String,
    val start_location: StartLocationDto,
    val steps: List<StepDto>,
    val traffic_speed_entry: List<Any>,
    val via_waypoint: List<Any>
)