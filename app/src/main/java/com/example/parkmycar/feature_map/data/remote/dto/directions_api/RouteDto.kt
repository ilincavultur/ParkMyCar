package com.example.parkmycar.feature_map.data.remote.dto.directions_api

data class RouteDto(
    val bounds: BoundsDto,
    val copyrights: String,
    val legs: List<LegDto>,
    val overview_polyline: OverviewPolylineDto,
    val summary: String,
    val warnings: List<Any>,
    val waypoint_order: List<Int>
)