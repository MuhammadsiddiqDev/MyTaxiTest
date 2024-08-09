package com.datasite.mytaxitestapp.core.state

import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng

data class MapState(
    val styleUrl: String = "https://api.maptiler.com/maps/streets-v2/style.json?key=DFTf41wLNmBwfqLyXxvi",
    val cameraPosition: CameraPosition = CameraPosition.Builder()
        .target(LatLng(41.311081, 69.280562))
        .zoom(15.0)
        .build()
)