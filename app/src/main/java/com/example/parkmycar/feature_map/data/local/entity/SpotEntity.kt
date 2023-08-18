package com.example.parkmycar.feature_map.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.parkmycar.feature_map.domain.models.MarkerType

@Entity(tableName = "spot_entity")
data class SpotEntity(
    @PrimaryKey
    val id: Int? = null,
    val name: String? = "",
    val lat: Double? = 0.0,
    val lng: Double? = 0.0,
    val type: MarkerType? = MarkerType.CAR_SPOT
)