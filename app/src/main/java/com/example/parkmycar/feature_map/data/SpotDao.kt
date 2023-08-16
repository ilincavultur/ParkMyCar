package com.example.parkmycar.feature_map.data

import androidx.room.*
import com.example.parkmycar.core.util.Resource
import kotlinx.coroutines.flow.Flow

@Dao
interface SpotDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpot(spot: SpotEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpots(spots: List<SpotEntity>)

    @Delete
    suspend fun deleteSpot(spot: SpotEntity)

    @Query("DELETE FROM spot_entity WHERE id IN(:spotIds)")
    suspend fun deleteSpots(spotIds: List<Int>)

    @Query("SELECT * FROM spot_entity")
    suspend fun getSpots(): List<SpotEntity>
}