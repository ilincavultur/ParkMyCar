package com.example.parkmycar.feature_map.domain.usecases

import com.example.parkmycar.core.util.Resource
import com.example.parkmycar.feature_map.data.remote.PlaceApi
import com.example.parkmycar.feature_map.domain.models.Spot
import com.example.parkmycar.feature_map.domain.repository.SpotRepository
import kotlinx.coroutines.flow.Flow

class FindParkingLots(
    private val repository: SpotRepository
) {
    operator fun invoke() : Flow<Resource<List<Spot>>> {
        return repository.findParkingLots()
    }
}