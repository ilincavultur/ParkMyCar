package com.example.parkmycar.feature_map.domain.repository

import com.example.parkmycar.feature_map.domain.models.Spot
import kotlinx.coroutines.flow.Flow

interface SpotRepository {
    suspend fun insertSpot(spot: Spot)

    suspend fun deleteSpot(spot: Spot)

    fun getSpots() : Flow<List<Spot>>
}