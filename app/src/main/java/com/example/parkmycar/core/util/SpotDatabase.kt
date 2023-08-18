package com.example.parkmycar.core.util

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.parkmycar.feature_map.data.local.SpotDao
import com.example.parkmycar.feature_map.data.local.entity.SpotEntity

@Database(
    entities = [SpotEntity::class],
    version = 1
)
abstract class SpotDatabase: RoomDatabase() {
    abstract val dao: SpotDao
}