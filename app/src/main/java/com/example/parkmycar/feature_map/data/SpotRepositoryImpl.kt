package com.example.parkmycar.feature_map.data

import android.content.ContentValues.TAG
import android.util.Log
import com.example.parkmycar.core.util.Resource
import com.example.parkmycar.feature_map.domain.models.Spot
import com.example.parkmycar.feature_map.domain.repository.SpotRepository
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow

import kotlinx.coroutines.flow.map
import java.io.IOException

class SpotRepositoryImpl(
    private val dao: SpotDao
) : SpotRepository {
    override suspend fun insertSpot(spot: Spot) {
        dao.insertSpot(spot = spot.toSpotEntity())
    }

    override suspend fun deleteSpot(spot: Spot) {
        dao.deleteSpot(spot = spot.toSpotEntity())
    }

    override fun getSpots(): Flow<Resource<List<Spot>>> = flow {
        emit(Resource.Loading())

        val spots = dao.getSpots().map {
            it.toSpot()
        }

        emit(Resource.Loading(data = spots))

        try {
            val remoteSpots = dao.getSpots()
            dao.deleteSpots(remoteSpots.map {
                it.id ?: 0
            })
            dao.insertSpots(remoteSpots)
        } catch (e: HttpException) {
            emit(Resource.Error(message = "Ooops something went wrong", data = spots))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Check internet connection", data = spots))
        }

        val newSpots = dao.getSpots().map {
            it.toSpot()
        }
        emit(Resource.Success(newSpots))
    }

    override suspend fun exists(id: Int): Boolean {
        return dao.exists(id)
    }

}