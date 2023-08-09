package com.example.parkmycar.di

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.example.parkmycar.core.util.SpotDatabase
import com.example.parkmycar.feature_map.data.SpotRepositoryImpl
import com.example.parkmycar.feature_map.domain.repository.SpotRepository
import com.example.parkmycar.feature_map.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    fun provideSpotRepository(db: SpotDatabase): SpotRepository {
        return SpotRepositoryImpl(db.dao)
    }

    @Provides
    @Singleton
    fun provideParkingUseCases(repository: SpotRepository): ParkingUseCases {
        return ParkingUseCases(
            findParkingLot = FindParkingLot(repository),
            findRouteToParkingLot = FindRouteToParkingLot(repository),
            getSavedSpot = GetSavedSpot(repository),
            savedSpot = SaveSpot(repository),
        )
    }
}