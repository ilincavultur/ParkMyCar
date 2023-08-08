package com.example.parkmycar.di

import android.app.Application
import androidx.room.Room
import com.example.parkmycar.core.util.SpotDatabase
import com.example.parkmycar.feature_map.data.SpotDao
import com.example.parkmycar.feature_map.data.SpotRepositoryImpl
import com.example.parkmycar.feature_map.domain.repository.SpotRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideSpotDatabase(app: Application): SpotDatabase {
        return Room.databaseBuilder(
            app,
            SpotDatabase::class.java,
            "spot_db.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideSpotRepository(db: SpotDatabase): SpotRepository {
        return SpotRepositoryImpl(db.dao)
    }
}