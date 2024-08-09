package com.datasite.mytaxitestapp.core.intentt

import com.mapbox.mapboxsdk.camera.CameraPosition

sealed class MapIntent {
    object LoadMap : MapIntent()
    data class SetCameraPosition(val position: CameraPosition) : MapIntent()
}