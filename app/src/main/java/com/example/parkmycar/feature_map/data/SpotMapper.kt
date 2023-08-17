package com.example.parkmycar.feature_map.data

import com.example.parkmycar.feature_map.domain.models.Spot

fun SpotEntity.toSpot() : Spot {
    return Spot(
        id = id,
        lat = lat,
        lng = lng,
        type = type,
        isSaved = true
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