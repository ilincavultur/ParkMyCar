package com.example.parkmycar.feature_map.presentation

import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import com.example.parkmycar.feature_map.domain.models.MarkerType
import com.example.parkmycar.feature_map.domain.models.Spot
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

val singapore = LatLng(1.3588227, 103.8742114)
val singapore2 = LatLng(1.40, 103.77)
val singapore3 = LatLng(1.90, 110.0)

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
    val defaultCameraPosition: CameraPosition = CameraPosition.fromLatLngZoom(singapore, 11f),
    val currentLocation: Location? = null,
    var isInShowRouteState: Boolean = false,
)