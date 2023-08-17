package com.example.parkmycar.feature_map.data

import com.example.parkmycar.feature_map.domain.models.Spot

fun SpotEntity.toSpot() : Spot {
    return Spot(
        id = id,
        lat = lat,
        lng = lng,
        type = type
    )
}

fun Spot.toSpotEntity() : SpotEntity {
    return SpotEntity(
        id = id,
        lat = lat,
        lng = lng,
        type = type
    )
}