package com.datasite.mytaxitestapp.core.state

import android.health.connect.datatypes.ExerciseRoute
import com.datasite.mytaxitestapp.core.room.UserLocation
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
data class MapState(
    val zoomLevel: Double = 14.0,
    val userLocation: ExerciseRoute.Location? = null,
    val locations: List<UserLocation> = emptyList(),
    val isLoading: Boolean = false,
    val showBottomSheet: Boolean = false
)