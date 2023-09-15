package com.example.parkmycar.feature_map.presentation

import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import com.example.parkmycar.feature_map.domain.models.MarkerType
import com.example.parkmycar.feature_map.domain.models.Spot
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

data class MapState(
    var isLoading: Boolean = false,
    val spots: List<Spot> = emptyList(),
    val parkingLots: List<Spot> = emptyList(),
    val path: MutableList<List<LatLng>> = mutableListOf(),
    val hiddenParkingLots: List<Spot> = emptyList(),
    var isMapLoaded: Boolean = false,
    var permissionsGranted: Boolean = false,
    var visiblePermissionDialogQueue: MutableList<String> = mutableStateListOf<String>(),
    var isAlertDialogDisplayed: Boolean = false,
    var spotToBeDeleted: Spot = Spot(),
    var isMarkerControlDialogDisplayed: Boolean = false,
    var spotToBeControlled: Spot = Spot(),
    val shouldAnimateZoom: Boolean = true,
    val defaultCameraPosition: CameraPosition = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 8f),
    val currentLocation: Location? = null,
    var isInShowRouteState: Boolean = false,
    var isInTrackingRouteState: Boolean = false,
    var isSnippetVisible: Boolean = false
)