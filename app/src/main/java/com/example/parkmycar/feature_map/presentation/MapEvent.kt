package com.example.parkmycar.feature_map.presentation

import com.example.parkmycar.feature_map.domain.models.Spot
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

sealed class MapEvent {
    data class OnMapLongClick(val latLng: LatLng): MapEvent()
    data class OnMarkerClick(val spot: Spot): MapEvent()
    data class OnMarkerLongClick(val spot: Spot): MapEvent()
    data class OnInfoWindowLongClick(val spot: Spot): MapEvent()
    object MapLoaded: MapEvent()
    object OnSearchButtonClick: MapEvent()
    object OnHideParkingSpotsToggleClick: MapEvent()
    object OnShowParkingSpotsToggleClick: MapEvent()
    object OnHideCarSpotsToggleClick: MapEvent()
    object OnShowCarSpotsToggleClick: MapEvent()
    object OnZoomOutClick: MapEvent()
    object OnZoomInClick: MapEvent()
}