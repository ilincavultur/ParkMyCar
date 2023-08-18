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
import com.google.android.gms.maps.CameraUpdateFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
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
                _state.value = state.value.copy(
                    isMarkerControlDialogDisplayed = true,
                    spotToBeControlled = event.spot
                )
            }
            is MapEvent.OnMapLongClick -> {
                Log.d(TAG, "onEvent: OnMapLongClick")
                viewModelScope.launch {
                    parkingUseCases.saveSpot(
                        Spot(
                            lat = event.latLng.latitude,
                            lng = event.latLng.longitude,
                            type = MarkerType.CAR_SPOT
                        )
                    )
                    loadMarkers()
                }
            }
            is MapEvent.OnInfoWindowClick -> {
                Log.d(TAG, "onEvent: OnMarkerLongClick")
                _state.value = state.value.copy(
                    isAlertDialogDisplayed = true,
                    spotToBeDeleted = event.spot
                )
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

                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    parkingUseCases.findParkingLots()
                        .onEach { result ->
                            when(result) {
                                is Resource.Error -> {
                                    _state.value = state.value.copy(
                                        parkingLots = result.data ?: emptyList(),
                                        isLoading = false
                                    )
                                    _eventFlow.emit(
                                        MapUiEvent.ShowSnackbar(
                                            result.message ?: "Unknown Error"
                                        ))
                                }
                                is Resource.Loading -> {
                                    _state.value = state.value.copy(
                                        parkingLots = result.data ?: emptyList(),
                                        isLoading = true
                                    )
                                }
                                is Resource.Success -> {
                                    _state.value = state.value.copy(
                                        parkingLots = result.data ?: emptyList(),
                                        isLoading = false
                                    )
                                }
                            }
                        }.launchIn(this)
                }

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
//            is MapEvent.OnMarkerClick -> {
//                Log.d(TAG, "onEvent: OnMarkerClick")
//            }
            is MapEvent.OnPermissionDialogDismiss -> {
                Log.d(TAG, "onEvent: OnPermissionDialogDismiss")
                _state.value.visiblePermissionDialogQueue.removeFirst()
            }
            is MapEvent.OnPermissionDialogResult -> {
                Log.d(TAG, "onEvent: OnPermissionDialogResult")
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
            is MapEvent.RemoveMarkerFromDb -> {
                Log.d(TAG, "onEvent: RemoveMarkerFromDb")
                viewModelScope.launch {
                    parkingUseCases.deleteSpotFromDb(
                        event.spot
                    )
                    loadMarkers()
                    withContext(Dispatchers.Main) {
                        _state.value = state.value.copy(
                            isAlertDialogDisplayed = false,
                            isMarkerControlDialogDisplayed = false
                        )
                    }
                }
            }
            is MapEvent.RemoveMarkerFromMap -> {
                Log.d(TAG, "onEvent: RemoveMarkerFromMap")
            }
            MapEvent.OnDismissRemoveMarkerFromDbClick -> {
                Log.d(TAG, "onEvent: OnDismissRemoveMarkerFromDbClick")
                _state.value = state.value.copy(
                    isAlertDialogDisplayed = false,
                    isMarkerControlDialogDisplayed = false
                )
            }
            MapEvent.OnGetRouteBtnClick -> {
                Log.d(TAG, "onEvent: OnGetRouteBtnClick")
            }
            MapEvent.OnDismissMarkerControllDialog -> {
                Log.d(TAG, "onEvent: OnDismissMarkerControllDialog")
                _state.value = state.value.copy(
                    isMarkerControlDialogDisplayed = false
                )
            }
            is MapEvent.OnZoomInClick -> {
                Log.d(TAG, "onEvent: OnZoomInClick")
                if (state.value.shouldAnimateZoom) {
                    viewModelScope.launch {
                        event.cameraPositionState.animate(CameraUpdateFactory.zoomIn())
                    }
                } else {
                    event.cameraPositionState.move(CameraUpdateFactory.zoomIn())
                }
            }
            is MapEvent.OnZoomOutClick -> {
                Log.d(TAG, "onEvent: OnZoomOutClick")
                if (state.value.shouldAnimateZoom) {
                    viewModelScope.launch {
                        event.cameraPositionState.animate(CameraUpdateFactory.zoomOut())
                    }
                } else {
                    event.cameraPositionState.move(CameraUpdateFactory.zoomOut())
                }
            }
            is MapEvent.OnSaveMarkerBtnClick -> {
                Log.d(TAG, "onEvent: OnSaveMarkerBtnClick")
                viewModelScope.launch {
                    parkingUseCases.saveSpot(
                        Spot(
                            name = event.spot.name,
                            lat = event.spot.lat,
                            lng = event.spot.lng,
                            type = event.spot.type
                        )
                    )
                    loadMarkers()
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