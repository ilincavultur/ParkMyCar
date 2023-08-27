package com.example.parkmycar.core.components.map

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.location.Location
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
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

val singapore = LatLng(1.35, 103.87)
private const val zoom = 8f

@SuppressLint("FlowOperatorInvokedInComposition", "MissingPermission")
@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    onMapLoaded: () -> Unit = {},
    content: @Composable () -> Unit = {},
    onMapLongClick: (LatLng) -> Unit,
    locationSource: MyLocationSource,
) {
    var uiSettings by remember { mutableStateOf(MapUiSettings(compassEnabled = false, zoomControlsEnabled = false)) }
    var mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL, isMyLocationEnabled = true))
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
            onMapLongClick(LatLng)
        },
        //locationSource = locationSource
    ) {
        content()
    }

}

