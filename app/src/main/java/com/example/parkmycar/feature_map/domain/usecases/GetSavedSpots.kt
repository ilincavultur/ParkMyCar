package com.example.parkmycar.feature_map.domain.usecases

import com.example.parkmycar.core.util.Resource
import com.example.parkmycar.feature_map.domain.models.Spot
import com.example.parkmycar.feature_map.domain.repository.SpotRepository
import kotlinx.coroutines.flow.Flow

class GetSavedSpots(
    private val repository: SpotRepository
) {
    suspend operator fun invoke() : Flow<Resource<List<Spot>>> {
        return repository.getSpots()
    }
}