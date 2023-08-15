package com.example.parkmycar.feature_map.presentation

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkmycar.feature_map.domain.usecases.ParkingUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val parkingUseCases: ParkingUseCases
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<MapUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _state = mutableStateOf(MapState())
    val state: State<MapState> = _state

//    init {
//        viewModelScope.launch {
//            _eventFlow.emit(
//                MapUiEvent.LaunchPermissionLauncher("hahahhaahha")
//            )
//        }
//    }

    fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.OnInfoWindowLongClick -> {
                Log.d(TAG, "onEvent: OnInfoWindowLongClick")
            }
            is MapEvent.OnMapLongClick -> {
                Log.d(TAG, "onEvent: OnMapLongClick")
            }
            is MapEvent.OnMarkerLongClick -> {
                Log.d(TAG, "onEvent: OnMarkerLongClick")
            }
            MapEvent.MapLoaded -> {
                if (state.value.permissionsGranted) {
                    _state.value = state.value.copy(
                        isMapLoaded = true
                    )
                }
            }
            MapEvent.OnSearchButtonClick -> {
                Log.d(TAG, "onEvent: OnSearchButtonClick")
            }
            MapEvent.OnHideCarSpotsToggleClick -> {
                Log.d(TAG, "onEvent: OnHideCarSpotsToggleClick")
            }
            MapEvent.OnHideParkingSpotsToggleClick -> {
                Log.d(TAG, "onEvent: OnHideParkingSpotsToggleClick")
            }
            MapEvent.OnShowCarSpotsToggleClick -> {
                Log.d(TAG, "onEvent: OnShowCarSpotsToggleClick")
            }
            MapEvent.OnShowParkingSpotsToggleClick -> {
                Log.d(TAG, "onEvent: OnShowParkingSpotsToggleClick")
            }
            MapEvent.OnZoomInClick -> {
                Log.d(TAG, "onEvent: OnZoomInClick")
            }
            MapEvent.OnZoomOutClick -> {
                Log.d(TAG, "onEvent: OnZoomOutClick")
            }
            is MapEvent.OnMarkerClick -> {
                Log.d(TAG, "onEvent: OnMarkerClick")
            }
            is MapEvent.OnPermissionDialogDismiss -> {
                _state.value.visiblePermissionDialogQueue.removeFirst()
            }
            is MapEvent.OnPermissionDialogResult -> {
                if(!event.isGranted && !state.value.visiblePermissionDialogQueue.contains(event.permission)) {
                    _state.value.visiblePermissionDialogQueue.add(event.permission)
                }
                if (event.isGranted) {
                    Log.d(TAG, "onEvent: i am jere permission granted")
                    // todo aici daca prima e acceptata nu mai asteapta si duipa a doua
                    _state.value = state.value.copy(
                        permissionsGranted = true
                    )
                }
            }
        }
    }
}