package com.datasite.mytaxitestapp.core.intentt

import com.google.android.gms.maps.model.CameraPosition
sealed class MapIntent {
    object LoadUserLocation : MapIntent()
    data class UpdateZoom(val zoomLevel: Double) : MapIntent()
    data class ToggleBottomSheet(val show: Boolean) : MapIntent()
}