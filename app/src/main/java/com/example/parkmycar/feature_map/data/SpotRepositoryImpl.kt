package com.example.parkmycar.feature_map.data

import com.example.parkmycar.feature_map.domain.models.Spot
import com.example.parkmycar.feature_map.domain.repository.SpotRepository
import kotlinx.coroutines.flow.Flow

class SpotRepositoryImpl(
    private val dao: SpotDao
) : SpotRepository {
    override suspend fun insertSpot(spot: Spot) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSpot(spot: Spot) {
        TODO("Not yet implemented")
    }

    override fun getSpots(): Flow<List<Spot>> {
        TODO("Not yet implemented")
    }

}