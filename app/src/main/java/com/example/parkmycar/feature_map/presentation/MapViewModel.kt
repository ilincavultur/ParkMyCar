package com.example.parkmycar.feature_map.presentation

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkmycar.core.util.Resource
import com.example.parkmycar.feature_map.domain.models.MarkerType
import com.example.parkmycar.feature_map.domain.models.Spot
import com.example.parkmycar.feature_map.domain.usecases.ParkingUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
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

    private var searchJob: Job? = null

    init {
        loadMarkers()
    }

    fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.OnInfoWindowLongClick -> {
                Log.d(TAG, "onEvent: OnInfoWindowLongClick")
            }
            is MapEvent.OnMapLongClick -> {
                Log.d(TAG, "onEvent: OnMapLongClick")
                viewModelScope.launch {
                    parkingUseCases.saveSpot(
                        Spot(
                            event.latLng.latitude,
                            event.latLng.longitude,
                            MarkerType.CAR_SPOT
                        )
                    )
                    loadMarkers()
                }
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
                if (!event.isGranted && !state.value.visiblePermissionDialogQueue.contains(event.permission)) {
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

    private fun loadMarkers() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            parkingUseCases.getSavedSpots()
                .onEach { result ->
                    when (result) {
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                spots = result.data ?: emptyList(),
                                isLoading = false
                            )
                            _eventFlow.emit(
                                MapUiEvent.ShowSnackbar(
                                    result.message ?: "Unknown Error"
                                )
                            )
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                spots = result.data ?: emptyList(),
                                isLoading = true
                            )
                        }
                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                spots = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }
                }.launchIn(this)
        }
    }
}