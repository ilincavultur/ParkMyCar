package com.example.parkmycar.feature_map.presentation

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkmycar.core.util.Resource
import com.example.parkmycar.feature_map.domain.models.MarkerType
import com.example.parkmycar.feature_map.domain.models.Spot
import com.example.parkmycar.feature_map.domain.usecases.ParkingUseCases
import com.example.parkmycar.feature_map.presentation.LocationService.Companion.ACTION_START
import com.example.parkmycar.feature_map.presentation.LocationService.Companion.ACTION_STOP
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.math.*

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
                Log.d(TAG, "onEvent: OnInfoWindowClick")
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
                println(state.value.currentLocation?.latitude)
                println(state.value.currentLocation?.longitude)
                loadParkingLots(LatLng(state.value.currentLocation?.latitude ?: 0.0, state.value.currentLocation?.longitude ?: 0.0))
            }
            MapEvent.OnHideCarSpotsToggleClick -> {
                Log.d(TAG, "onEvent: OnHideCarSpotsToggleClick")
                _state.value = state.value.copy(
                    spots = state.value.spots.filter {
                        it.type == MarkerType.PARKING_SPOT
                    }
                )
            }
            MapEvent.OnHideParkingSpotsToggleClick -> {
                Log.d(TAG, "onEvent: OnHideParkingSpotsToggleClick")
                _state.value = state.value.copy(
                    spots = state.value.spots.filter {
                        it.type == MarkerType.CAR_SPOT
                    },
                    hiddenParkingLots = state.value.parkingLots,
                    parkingLots = emptyList()
                )
            }
            MapEvent.OnShowCarSpotsToggleClick -> {
                Log.d(TAG, "onEvent: OnShowCarSpotsToggleClick")
                    viewModelScope.launch {
                    parkingUseCases.getSavedSpots()
                        .onEach { result ->
                            when(result) {
                                is Resource.Error -> {
                                    _state.value = state.value.copy(
                                        spots = state.value.spots.plus(result.data?.filter {
                                            it.type == MarkerType.CAR_SPOT
                                        } ?: emptyList()) ,
                                        isLoading = false
                                    )
                                    _eventFlow.emit(
                                        MapUiEvent.ShowSnackbar(
                                            result.message ?: "Unknown Error"
                                        ))
                                }
                                is Resource.Loading -> {
                                    _state.value = state.value.copy(
                                        spots = state.value.spots.plus(result.data?.filter {
                                            it.type == MarkerType.CAR_SPOT
                                        } ?: emptyList()) ,
                                        isLoading = true
                                    )
                                }
                                is Resource.Success -> {
                                    _state.value = state.value.copy(
                                        spots = state.value.spots.plus(result.data?.filter {
                                            it.type == MarkerType.CAR_SPOT
                                        } ?: emptyList()) ,
                                        isLoading = false
                                    )
                                }
                            }
                        }.launchIn(this)
                }
            }
            MapEvent.OnShowParkingSpotsToggleClick -> {
                Log.d(TAG, "onEvent: OnShowParkingSpotsToggleClick")
                viewModelScope.launch {
                parkingUseCases.getSavedSpots()
                    .onEach { result ->
                        when(result) {
                            is Resource.Error -> {
                                _state.value = state.value.copy(
                                    spots = state.value.spots.plus(result.data?.filter {
                                        it.type == MarkerType.PARKING_SPOT
                                    } ?: emptyList()) ,
                                    isLoading = false
                                )
                                _eventFlow.emit(
                                    MapUiEvent.ShowSnackbar(
                                        result.message ?: "Unknown Error"
                                    ))
                            }
                            is Resource.Loading -> {
                                _state.value = state.value.copy(
                                    spots = state.value.spots.plus(result.data?.filter {
                                        it.type == MarkerType.PARKING_SPOT
                                    } ?: emptyList()) ,
                                    isLoading = true
                                )
                            }
                            is Resource.Success -> {
                                _state.value = state.value.copy(
                                    spots = state.value.spots.plus(result.data?.filter {
                                        it.type == MarkerType.PARKING_SPOT
                                    } ?: emptyList()) ,
                                    isLoading = false
                                )
                            }
                        }
                    }.launchIn(this)
                }
                _state.value = state.value.copy(
                    parkingLots = state.value.hiddenParkingLots,
                    hiddenParkingLots = emptyList()
                )
            }
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
                    withContext(Dispatchers.Main) {
                        _state.value = state.value.copy(
                            isAlertDialogDisplayed = false,
                            isMarkerControlDialogDisplayed = false
                        )
                    }
                }
                loadMarkers()
            }
            MapEvent.OnDismissRemoveMarkerFromDbClick -> {
                Log.d(TAG, "onEvent: OnDismissRemoveMarkerFromDbClick")
                _state.value = state.value.copy(
                    isAlertDialogDisplayed = false,
                    isMarkerControlDialogDisplayed = false
                )
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
                    withContext(Dispatchers.Main) {
                        _state.value = state.value.copy(
                            isMarkerControlDialogDisplayed = false
                        )
                    }
                }
                loadMarkers()
            }
            is MapEvent.OnGetRouteBtnClick -> {
                Log.d(TAG, "onEvent: OnGetRouteBtnClick")
                viewModelScope.launch(Dispatchers.Default) {
                    parkingUseCases.computeRoute(event.source, event.destination).onEach { result ->
                        when (result) {
                            is Resource.Error -> {
                                _state.value = state.value.copy(
                                    path = result.data ?: mutableListOf(),
                                    isLoading = false,
                                    isInShowRouteState = false,
                                    isMarkerControlDialogDisplayed = false,
                                )
                                _eventFlow.emit(
                                    MapUiEvent.ShowSnackbar(
                                        result.message ?: "Unknown Error"
                                    )
                                )
                            }
                            is Resource.Loading -> {
                                _state.value = state.value.copy(
                                    path = result.data ?: mutableListOf(),
                                    isLoading = true,
                                    isInShowRouteState = true,
                                    isMarkerControlDialogDisplayed = false,
                                    isSnippetVisible = false
                                )
                            }
                            is Resource.Success -> {
                                _state.value = state.value.copy(
                                    path = result.data ?: mutableListOf(),
                                    isLoading = false,
                                    isInShowRouteState = true,
                                    isMarkerControlDialogDisplayed = false,
                                    isSnippetVisible = false
                                )
                            }
                        }
                    }.launchIn(this)
                }
            }
            is MapEvent.UpdateLocation -> {
                var newPath = mutableListOf<List<LatLng>>()

                if (state.value.path.isNotEmpty() && state.value.path.first().size >= 2) {
                    viewModelScope.launch {
                        val currentPath = state.value.path.first()
                        val distanceFirstLatLng = distance(state.value.path.first()[0].latitude, state.value.path.first()[0].longitude, event.location.latitude, event.location.longitude)
                        val distanceSecondLatLng = distance(state.value.path.first()[1].latitude, state.value.path.first()[1].longitude, event.location.latitude, event.location.longitude)

                        val size = state.value.path.first().size-1
                        newPath = mutableListOf(
                            if (distanceFirstLatLng < 0.1 || distanceSecondLatLng < 0.1) {
                                state.value.path.first().slice(indices = IntRange(2,size))
                            } else {
                                //currentPath
                                emptyList()
                            }
                        )
                    }
                }

                _state.value = state.value.copy(
                    currentLocation = event.location,
                )
                if (newPath.isNotEmpty() && newPath.first().isNotEmpty()) {
                    _state.value = state.value.copy(
                        path = newPath
                    )
                }
            }
            is MapEvent.ToggleLocationTrackingService -> {
                viewModelScope.launch {
                    Intent(event.context, LocationService::class.java).apply {
                        action = if (state.value.isInTrackingRouteState) ACTION_STOP else ACTION_START
                        ActivityCompat.startForegroundService(event.context, this)
                    }
                }
                _state.value = state.value.copy(
                    isInTrackingRouteState = !state.value.isInTrackingRouteState
                )
            }
            is MapEvent.ToggleShowRouteState -> {
                if (state.value.isInTrackingRouteState) {
                    viewModelScope.launch {
                        Intent(event.context, LocationService::class.java).apply {
                            action = ACTION_STOP
                            ActivityCompat.startForegroundService(event.context, this)
                        }
                    }
                }

                _state.value = state.value.copy(
                    isInShowRouteState = false,
                    isInTrackingRouteState = false,
                    path = mutableListOf()
                )
            }
            is MapEvent.OnCarSpotMarkerClick -> {
                _state.value = state.value.copy(
                    isSnippetVisible = true
                )
            }
        }
    }

    private fun loadMarkers() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
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

    private fun loadParkingLots(latLng: LatLng) {
        Log.d(TAG, "onEvent: loadParkingLots")
        println(state.value.currentLocation?.latitude)
        println(state.value.currentLocation?.longitude)
        viewModelScope.launch {
            parkingUseCases.findParkingLots(latLng)
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
            loadMarkers()
        }
    }
}

// START: https://stackoverflow.com/questions/18170131/comparing-two-locations-using-their-longitude-and-latitude
private fun distance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
    val earthRadius = 3958.75 // in miles, change to 6371 for kilometer output

    val dLat = Math.toRadians(lat2 - lat1)
    val dLng = Math.toRadians(lng2 - lng1)

    val sindLat = sin(dLat / 2)
    val sindLng = sin(dLng / 2)

    val a = sindLat.pow(2.0) +
            (sindLng.pow(2.0) * cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)))

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return earthRadius * c // output distance, in MILES
}
// END: https://stackoverflow.com/questions/18170131/comparing-two-locations-using-their-longitude-and-latitude