package com.example.parkmycar.feature_map.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.parkmycar.feature_map.domain.models.MarkerType

@Entity(tableName = "spot_entity")
data class SpotEntity(
    @PrimaryKey
    val id: Int? = null,
    val lat: Double,
    val lng: Double,
    val type: MarkerType
)