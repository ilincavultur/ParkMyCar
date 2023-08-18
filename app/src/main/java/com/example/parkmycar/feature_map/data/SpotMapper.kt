package com.example.parkmycar.feature_map.data

import com.example.parkmycar.feature_map.data.local.entity.SpotEntity
import com.example.parkmycar.feature_map.domain.models.Spot

fun SpotEntity.toSpot() : Spot {
    return Spot(
        id = id,
        name = name,
        lat = lat,
        lng = lng,
        type = type,
        isSaved = true
    )
}

fun Spot.toSpotEntity() : SpotEntity {
    return SpotEntity(
        id = id,
        name = name,
        lat = lat,
        lng = lng,
        type = type
    )
}