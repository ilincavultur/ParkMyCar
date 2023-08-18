package com.example.parkmycar.feature_map.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.example.parkmycar.BuildConfig
import com.example.parkmycar.core.util.Resource
import com.example.parkmycar.feature_map.data.local.SpotDao
import com.example.parkmycar.feature_map.data.remote.PlaceApi
import com.example.parkmycar.feature_map.data.remote.dto.toSpot
import com.example.parkmycar.feature_map.data.toSpot
import com.example.parkmycar.feature_map.data.toSpotEntity
import com.example.parkmycar.feature_map.domain.models.Spot
import com.example.parkmycar.feature_map.domain.repository.SpotRepository
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import java.io.IOException

class SpotRepositoryImpl(
    private val api: PlaceApi,
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

    override fun findParkingLots(): Flow<Resource<List<Spot>>> = flow {
        emit(Resource.Loading())
        Log.d(TAG, "findParkingLots: i am here first")
        val lat = 1.35
        val lng = 103.87
        try {
            val remoteSpots = api.findParkingLots(
                fields = "formatted_address,name,geometry",
                input = "parking",
                inputtype = "textquery",
                locationbias = "circle:radius@${lat},${lng}",
                apiKey = BuildConfig.MAPS_API_KEY,
            )
            val newParkingLots = remoteSpots.candidates.map {
                it.toSpot()
            }
            Log.d(TAG, "findParkingLots: i am here")
            remoteSpots.candidates.forEach {
                Log.d(TAG, "findParkingLots: placedto: " + it.formatted_address + " " + it.name + "\n" +
                    it.geometry.location.lat + "\n" +
                        it.geometry.location.lng + "\n"
                )
            }
            emit(Resource.Success(newParkingLots))

            /*
            https://maps.googleapis.com/maps/api/place/findplacefromtext/json
  ?fields=formatted_address%2Cname%2Crating%2Copening_hours%2Cgeometry
  &input=mongolian
  &inputtype=textquery
  &locationbias=circle%3A2000%4047.6918452%2C-122.2226413
  &key=YOUR_API_KEY
             */

        } catch (e: HttpException) {
            emit(Resource.Error(message = "Ooops something went wrong", data = emptyList()))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Check internet connection", data = emptyList()))
        }

        //emit(Resource.Success(remoteSpots))
    }

}