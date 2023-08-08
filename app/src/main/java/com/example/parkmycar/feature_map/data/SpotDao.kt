package com.example.parkmycar.feature_map.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SpotDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpot(spot: SpotEntity)

    @Delete
    suspend fun deleteSpot(spot: SpotEntity)

    @Query("SELECT * FROM spot_entity")
    fun getSpots(): Flow<List<SpotEntity>>
}