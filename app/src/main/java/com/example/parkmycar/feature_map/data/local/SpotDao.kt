package com.example.parkmycar.feature_map.data.local

import androidx.room.*
import com.example.parkmycar.feature_map.data.local.entity.SpotEntity

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

    @Query("SELECT EXISTS (SELECT 1 FROM spot_entity WHERE id = :id)")
    suspend fun exists(id: Int): Boolean
}