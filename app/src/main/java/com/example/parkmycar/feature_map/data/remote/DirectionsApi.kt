package com.example.parkmycar.feature_map.data.remote

import com.example.parkmycar.feature_map.data.remote.dto.FindPlaceResponse
import com.example.parkmycar.feature_map.data.remote.dto.directions_api.DirectionsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionsApi {
    @GET("json")
    suspend fun computeRoute(@Query("origin") origin: String, @Query("destination") destination: String, @Query("key") apiKey: String) : DirectionsResponse
}