package com.example.parkmycar.feature_map.domain.usecases

import android.content.ContentValues
import android.util.Log
import com.example.parkmycar.core.util.Resource
import com.example.parkmycar.feature_map.data.remote.PlaceApi
import com.example.parkmycar.feature_map.domain.models.Spot
import com.example.parkmycar.feature_map.domain.repository.SpotRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

class FindParkingLots(
    private val repository: SpotRepository
) {
    operator fun invoke(latLng: LatLng) : Flow<Resource<List<Spot>>> {
        return repository.findParkingLots(latLng)
    }
}