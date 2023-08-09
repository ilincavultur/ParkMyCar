package com.example.parkmycar.feature_map.domain.usecases

import com.example.parkmycar.feature_map.domain.repository.SpotRepository

class FindRouteToParkingLot(
    private val repository: SpotRepository
) {
}