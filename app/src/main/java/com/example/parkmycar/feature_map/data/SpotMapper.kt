package com.example.parkmycar.feature_map.data

import com.example.parkmycar.feature_map.domain.models.Spot

fun SpotEntity.toSpot() : Spot {
    return Spot(
        lat = lat,
        lng = lng,
        type = type
    )
}

fun Spot.toSpotEntity() : SpotEntity {
    return SpotEntity(
        id = null,
        lat = lat,
        lng = lng,
        type = type
    )
}