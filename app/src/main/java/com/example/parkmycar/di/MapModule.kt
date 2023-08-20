package com.example.parkmycar.di

import android.content.Context
import androidx.room.Room
import com.example.parkmycar.core.util.SpotDatabase
import com.example.parkmycar.feature_map.data.remote.DirectionsApi
import com.example.parkmycar.feature_map.data.remote.PlaceApi
import com.example.parkmycar.feature_map.data.repository.SpotRepositoryImpl
import com.example.parkmycar.feature_map.domain.repository.SpotRepository
import com.example.parkmycar.feature_map.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapModule {
    @Singleton
    @Provides
    fun provideSpotDatabase(
        @ApplicationContext appContext: Context
    ): SpotDatabase {
        return Room.databaseBuilder(
            appContext,
            SpotDatabase::class.java,
            "spot_db.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideSpotDao(
        database: SpotDatabase
    ) = database.dao

    @Singleton
    @Provides
    fun provideSpotRepository(
        placeApi: PlaceApi,
        directionsApi: DirectionsApi,
        db: SpotDatabase
    ): SpotRepository {
        return SpotRepositoryImpl(placeApi, directionsApi, db.dao)
    }

    @Provides
    @Singleton
    fun provideParkingUseCases(repository: SpotRepository): ParkingUseCases {
        return ParkingUseCases(
            findParkingLots = FindParkingLots(repository),
            findRouteToParkingLot = FindRouteToParkingLot(repository),
            getSavedSpot = GetSavedSpot(repository),
            getSavedSpots = GetSavedSpots(repository),
            saveSpot = SaveSpot(repository),
            deleteSpotFromDb = DeleteSpotFromDb(repository),
            checkIfSaved = CheckIfSaved(repository),
            computeRoute = ComputeRoute(repository)
        )
    }

    @Singleton
    @Provides
    fun providePlaceFindApi(): PlaceApi = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/maps/api/place/findplacefromtext/")
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PlaceApi::class.java)

    @Singleton
    @Provides
    fun provideDirectionsApi(): DirectionsApi = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/maps/api/directions/")
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DirectionsApi::class.java)
}