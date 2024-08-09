package com.datasite.mytaxitestapp.core.viewModel

import androidx.lifecycle.ViewModel
import com.datasite.mytaxitestapp.core.intentt.MapIntent
import com.datasite.mytaxitestapp.core.state.MapState
import com.mapbox.mapboxsdk.camera.CameraPosition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MapViewModel : ViewModel() {

    private val _state = MutableStateFlow(MapState())
    val state: StateFlow<MapState> get() = _state

    fun processIntent(intent: MapIntent) {
        when (intent) {
            is MapIntent.LoadMap -> loadMap()
            is MapIntent.SetCameraPosition -> setCameraPosition(intent.position)
        }
    }

    private fun loadMap() {
        // Update state or perform actions to load the map
        // Example: _state.value = _state.value.copy(styleUrl = "newStyleUrl")
    }

    private fun setCameraPosition(position: CameraPosition) {
        _state.value = _state.value.copy(cameraPosition = position)
    }
}