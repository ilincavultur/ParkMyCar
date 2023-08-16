package com.example.parkmycar.core.components.map

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.parkmycar.R
import com.example.parkmycar.feature_map.domain.models.MarkerType
import com.example.parkmycar.feature_map.domain.models.Spot
import com.example.parkmycar.feature_map.presentation.*
import com.example.parkmycar.ui.theme.ParkMyCarTheme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

val singapore = LatLng(1.35, 103.87)

@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    onMapLoaded: () -> Unit = {},
    content: @Composable () -> Unit = {},
    viewModel: MapViewModel
) {
    val localContext = LocalContext.current
    // all of these into the viewmodel

    val singaporeState = rememberMarkerState(position = singapore)
    val showParkingMarkers = remember { mutableStateOf(true) }
    val showCarMarkers = remember { mutableStateOf(true) }

    var circleCenter by remember { mutableStateOf(singapore) }
    if (singaporeState.dragState == DragState.END) {
        circleCenter = singaporeState.position
    }

    var uiSettings by remember { mutableStateOf(MapUiSettings(compassEnabled = false, zoomControlsEnabled = false)) }
    var shouldAnimateZoom by remember { mutableStateOf(true) }
    var ticker by remember { mutableStateOf(0) }
    var mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = uiSettings,
        onMapLoaded = onMapLoaded,
        onPOIClick = {
            Log.d(TAG, "POI clicked: ${it.name}")
        },
        onMapLongClick = { LatLng ->
            viewModel.onEvent(MapEvent.OnMapLongClick(LatLng))
        }
    ) {
//            val markerClick: (Marker) -> Boolean = { marker ->
//                viewModel.onEvent(MapEvent.OnMarkerClick(Spot(
//                    marker.position.latitude, marker.position.longitude, MarkerType.PARKING_SPOT
//                )))
//                false
//            }

//            CustomMarker(
//                context = localContext,
//                state = singaporeState,
//                iconResourceId = R.drawable.ic_baseline_local_parking_24,
//                markerClick = markerClick,
//                isSaved = false,
//                type = MarkerType.PARKING_SPOT,
//                onInfoWindowLongClick = { marker ->
//                    viewModel.onEvent(MapEvent.OnMarkerLongClick(Spot(
//                        marker.position.latitude, marker.position.longitude, MarkerType.PARKING_SPOT
//                    )))
//                }
//            )


        viewModel.state.value.spots.forEach { spot ->
            Log.d(TAG, "MapScreen: i am here")
            Marker(
                state = MarkerState(LatLng(spot.lat, spot.lng)),
                title = "Parking spot (${spot.lat}, ${spot.lng})",
                snippet = "Long click to delete",
                onInfoWindowLongClick = {
                    viewModel.onEvent(
                        MapEvent.OnInfoWindowLongClick(spot)
                    )
                },
                onClick = {
                    it.showInfoWindow()
                    true
                },
                icon = BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_GREEN
                )
            )
        }
        content()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
            ){
                val coroutineScope = rememberCoroutineScope()
                ZoomControls(
                    onZoomOut = {
                        viewModel.onEvent(MapEvent.OnZoomOutClick)
                    },
                    onZoomIn = {
                        viewModel.onEvent(MapEvent.OnZoomInClick)
                    }
                )
            }

            Column (
                horizontalAlignment = Alignment.End,
            ) {
                MapButton(
                    text = "",
                    onClick = {
                              viewModel.onEvent(MapEvent.OnSearchButtonClick)
//                        mapProperties = mapProperties.copy(mapType = MapType.NORMAL)
//                        cameraPositionState.position = defaultCameraPosition
//                        singaporeState.position = singapore
//                        singaporeState.hideInfoWindow()
                    },
                    icon = Icons.Default.Search
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
            ) {
                SwitchButton(
                    //mapProperties.copy(mapType = MapType.NORMAL)
                    onCheckedChange = {
                        if (it) {
                            viewModel.onEvent(MapEvent.OnShowParkingSpotsToggleClick)
                        } else {
                            viewModel.onEvent(MapEvent.OnHideParkingSpotsToggleClick)
                        }
                    },
                    checkedTrackColor = Color(0xFF0029FF),
                    uncheckedTrackColor = Color(0x3E0029FF)
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
            ) {
                SwitchButton(
                    onCheckedChange = {
                        if (it) {
                            viewModel.onEvent(MapEvent.OnShowCarSpotsToggleClick)
                        } else {
                            viewModel.onEvent(MapEvent.OnHideCarSpotsToggleClick)
                        }
                    },
                    checkedTrackColor = Color(0xFF673AB7),
                    uncheckedTrackColor = Color(0x3D673AB7)
                )
            }
        }

        //DebugView(cameraPositionState, singaporeState)
    }
}


//@Composable
//private fun MapTypeControls(
//    onMapTypeClick: (MapType) -> Unit
//) {
//    Row(
//        Modifier
//            .fillMaxWidth()
//            .horizontalScroll(state = ScrollState(0)),
//        horizontalArrangement = Arrangement.Center
//    ) {
//
//        MapTypeButton(type = MapType.NORMAL) {}
//
//    }
//}

//@Composable
//private fun MapTypeButton(type: MapType, onClick: () -> Unit) =
//    MapButton(text = type.toString(), onClick = onClick)





@Composable
private fun DebugView(
    cameraPositionState: CameraPositionState,
    markerState: MarkerState
) {
    Column(
        Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        val moving =
            if (cameraPositionState.isMoving) "moving" else "not moving"
        Text(text = "Camera is $moving")
        Text(text = "Camera position is ${cameraPositionState.position}")
        Spacer(modifier = Modifier.height(4.dp))
        val dragging =
            if (markerState.dragState == DragState.DRAG) "dragging" else "not dragging"
        Text(text = "Marker is $dragging")
        Text(text = "Marker position is ${markerState.position}")
    }
}


//@Preview
//@Composable
//fun GoogleMapViewPreview() {
//    ParkMyCarTheme{
//        GoogleMapView(Modifier.fillMaxSize())
//    }
//}