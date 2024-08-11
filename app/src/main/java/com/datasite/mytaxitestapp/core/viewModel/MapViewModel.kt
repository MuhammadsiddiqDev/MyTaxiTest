package com.datasite.mytaxitestapp.core.viewModel

import androidx.lifecycle.ViewModel
import com.datasite.mytaxitestapp.core.intentt.MapIntent
import com.datasite.mytaxitestapp.core.state.MapState
import com.google.android.gms.maps.model.CameraPosition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
class MapViewModel : ViewModel() {
    private val _state = MutableStateFlow(MapState())
    val state: StateFlow<MapState> = _state.asStateFlow()

    fun handleIntent(intent: MapIntent) {
        when (intent) {
            is MapIntent.LoadUserLocation -> loadUserLocation()
            is MapIntent.UpdateZoom -> updateZoom(intent.zoomLevel)
            is MapIntent.ToggleBottomSheet -> toggleBottomSheet(intent.show)
        }
    }

    private fun loadUserLocation() {
        // Implement your logic to load user location
    }

    private fun updateZoom(zoomLevel: Double) {
        _state.value = _state.value.copy(zoomLevel = zoomLevel)
    }

    private fun toggleBottomSheet(show: Boolean) {
        _state.value = _state.value.copy(showBottomSheet = show)
    }
}