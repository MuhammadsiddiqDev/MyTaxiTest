package com.datasite.mytaxitestapp.core.room
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserLocationDao {
    @Insert
    suspend fun insertLocation(location: UserLocation)

    @Query("SELECT * FROM user_locations")
    fun getAllLocations(): Flow<List<UserLocation>>
}