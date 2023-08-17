package com.example.parkmycar.feature_map.domain.repository

import com.example.parkmycar.core.util.Resource
import com.example.parkmycar.feature_map.domain.models.Spot
import kotlinx.coroutines.flow.Flow

interface SpotRepository {
    suspend fun insertSpot(spot: Spot)

    suspend fun deleteSpot(spot: Spot)

    fun getSpots() : Flow<Resource<List<Spot>>>

    suspend fun exists(id: Int) : Boolean
}