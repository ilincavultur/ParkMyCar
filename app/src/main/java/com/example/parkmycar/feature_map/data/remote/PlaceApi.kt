package com.example.parkmycar.feature_map.data.remote

import com.example.parkmycar.feature_map.data.remote.dto.FindPlaceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceApi {
    @GET("json")
    suspend fun findParkingLots(@Query("fields") fields: String, @Query("input") input: String, @Query("inputtype") inputtype: String, @Query("locationbias") locationbias: String, @Query("key") apiKey: String) : FindPlaceResponse
}