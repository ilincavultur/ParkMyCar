package com.example.parkmycar.core.components.map

import android.content.ContentValues.TAG
import android.util.Log
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.parkmycar.feature_map.presentation.defaultCameraPosition
import com.example.parkmycar.feature_map.presentation.singapore2
import com.example.parkmycar.feature_map.presentation.singapore3
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
    content: @Composable () -> Unit = {}
) {
    val singaporeState = rememberMarkerState(position = singapore)
    val singapore2State = rememberMarkerState(position = singapore2)
    val singapore3State = rememberMarkerState(position = singapore3)

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
    var mapVisible by remember { mutableStateOf(true) }

    if (mapVisible) {
        GoogleMap(
            modifier = modifier,
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = uiSettings,
            onMapLoaded = onMapLoaded,
            onPOIClick = {
                Log.d(TAG, "POI clicked: ${it.name}")
            }
        ) {
            // Drawing on the map is accomplished with a child-based API
            val markerClick: (Marker) -> Boolean = {
                Log.d(TAG, "${it.title} was clicked")
                cameraPositionState.projection?.let { projection ->
                    Log.d(TAG, "The current projection is: $projection")
                }
                false
            }
            MarkerInfoWindowContent(
                state = singaporeState,
                title = "Zoom in has been tapped $ticker times.",
                onClick = markerClick,
                draggable = true,
            ) {
                Text(it.title ?: "Title", color = Color.Red)
            }
            MarkerInfoWindowContent(
                state = singapore2State,
                title = "Marker with custom info window.\nZoom in has been tapped $ticker times.",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                onClick = markerClick,
            ) {
                Text(it.title ?: "Title", color = Color.Blue)
            }
            Marker(
                state = singapore3State,
                title = "Marker in Singapore",
                onClick = markerClick
            )
            Circle(
                center = circleCenter,
                fillColor = MaterialTheme.colors.secondary,
                strokeColor = MaterialTheme.colors.secondaryVariant,
                radius = 1000.0,
            )

            content()

        }

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
                        if (shouldAnimateZoom) {
                            coroutineScope.launch {
                                cameraPositionState.animate(CameraUpdateFactory.zoomOut())
                            }
                        } else {
                            cameraPositionState.move(CameraUpdateFactory.zoomOut())
                        }
                    },
                    onZoomIn = {
                        if (shouldAnimateZoom) {
                            coroutineScope.launch {
                                cameraPositionState.animate(CameraUpdateFactory.zoomIn())
                            }
                        } else {
                            cameraPositionState.move(CameraUpdateFactory.zoomIn())
                        }
                        ticker++
                    }
                )
            }

            Column (
                horizontalAlignment = Alignment.End,
            ) {
                MapButton(
                    text = "",
                    onClick = {
                        mapProperties = mapProperties.copy(mapType = MapType.NORMAL)
                        cameraPositionState.position = defaultCameraPosition
                        singaporeState.position = singapore
                        singaporeState.hideInfoWindow()
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
                    onCheckedChange = {

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


@Preview
@Composable
fun GoogleMapViewPreview() {
    ParkMyCarTheme{
        GoogleMapView(Modifier.fillMaxSize())
    }
}