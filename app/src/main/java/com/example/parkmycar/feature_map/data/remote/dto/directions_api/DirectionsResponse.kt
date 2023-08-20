package com.example.parkmycar.feature_map.data.remote.dto.directions_api

data class DirectionsResponse(
    val geocoded_waypoints: List<GeocodedWaypointDto>,
    val routes: List<RouteDto>,
    val status: String
)