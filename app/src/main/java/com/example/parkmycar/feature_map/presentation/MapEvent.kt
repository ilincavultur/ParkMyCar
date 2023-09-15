package com.example.parkmycar.feature_map.presentation

import android.content.Context
import android.location.Location
import com.example.parkmycar.feature_map.domain.models.Spot
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.CameraPositionState

sealed class MapEvent {
    data class OnMapLongClick(val latLng: LatLng): MapEvent()
    object OnCarSpotMarkerClick: MapEvent()
    data class OnInfoWindowClick(val spot: Spot): MapEvent() // does not exist
    data class OnInfoWindowLongClick(val spot: Spot): MapEvent()
    object MapLoaded: MapEvent()
    object OnSearchButtonClick: MapEvent()
    object OnHideParkingSpotsToggleClick: MapEvent()
    object OnShowParkingSpotsToggleClick: MapEvent()
    object OnHideCarSpotsToggleClick: MapEvent()
    object OnShowCarSpotsToggleClick: MapEvent()
    data class OnZoomOutClick(val cameraPositionState: CameraPositionState): MapEvent()
    data class OnZoomInClick(val cameraPositionState: CameraPositionState): MapEvent()
    data class OnPermissionDialogResult(val permission: String, val isGranted: Boolean): MapEvent()
    object OnPermissionDialogDismiss: MapEvent()
    object OnDismissRemoveMarkerFromDbClick: MapEvent()
    data class RemoveMarkerFromDb(val spot: Spot): MapEvent() // remove from map and db
    data class OnGetRouteBtnClick(val source: String, val destination: String): MapEvent()
    object OnDismissMarkerControllDialog: MapEvent()
    data class OnSaveMarkerBtnClick(val spot: Spot): MapEvent()
    data class UpdateLocation(val location: Location): MapEvent()
    data class ToggleLocationTrackingService(val context: Context): MapEvent()
    data class ToggleShowRouteState(val context: Context): MapEvent()
}