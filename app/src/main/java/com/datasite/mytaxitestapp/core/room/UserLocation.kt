package com.datasite.mytaxitestapp.core.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_locations")
data class UserLocation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val latitude: Double,
    val longitude: Double
)