package com.example.parkmycar.feature_map.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.parkmycar.R
import com.example.parkmycar.core.components.map.*
import com.example.parkmycar.core.components.permission.CoarseLocationPermissionTextProvider
import com.example.parkmycar.core.components.permission.CustomPermissionDialog
import com.example.parkmycar.core.components.permission.FineLocationPermissionTextProvider
import com.example.parkmycar.feature_map.domain.models.MarkerType
import com.example.parkmycar.fusedLocationClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit

val locationSource = MyLocationSource()
private val permissionsToRequest = arrayOf(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION,
)


internal fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}

@SuppressLint("FlowOperatorInvokedInComposition")
@RequiresPermission(
    anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION],
)
@Composable
fun MapScreen(
    navController: NavController,
    viewModel: MapViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
) {
    val state = viewModel.state.value
    val localContext = LocalContext.current
    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            permissionsToRequest.forEach { permission ->
                viewModel.onEvent(MapEvent.OnPermissionDialogResult(
                    permission = permission,
                    isGranted = perms[permission] == true
                ))
            }
        }
    )
    val drawPolylines = mutableListOf<LatLng>()

    val firstLocation = remember {
      mutableStateOf(true)
    }
    val cameraPositionState = rememberCameraPositionState {
        position = state.defaultCameraPosition
    }

    //LaunchedEffect(true) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if (location != null) {
                    //Log.d(TAG, "MapScreen: UpdateLocation")
                    viewModel.onEvent(MapEvent.UpdateLocation(location))
                }
            }
    //}


//    // Update blue dot and camera when the location changes
    LaunchedEffect(state.currentLocation) {
        //Log.d(TAG, "Updating blue dot on map...")
        if (state.isMapLoaded) {
            state.currentLocation?.let {
                locationSource.onLocationChanged(it)
                if (!state.isInTrackingRouteState) {
                    if (firstLocation.value) {
                        cameraPositionState.move(
                            CameraUpdateFactory.newLatLng(
                                LatLng(
                                    it.latitude,
                                    it.longitude
                                )
                            )
                        )
                        firstLocation.value = false
                    }
                } else {
                    cameraPositionState.move(
                        CameraUpdateFactory.newLatLng(
                            LatLng(
                                it.latitude,
                                it.longitude
                            )
                        )
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is MapUiEvent.LaunchPermissionLauncher -> TODO()
                is MapUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    if (state.permissionsGranted) {
        Box(Modifier.fillMaxSize()) {
            GoogleMapView(
                modifier = Modifier.matchParentSize(),
                cameraPositionState = cameraPositionState,
                onMapLoaded = {
                    viewModel.onEvent(MapEvent.MapLoaded)
                },
                onMapLongClick = { latLng ->
                    viewModel.onEvent(MapEvent.OnMapLongClick(latLng))
                },
                locationSource = locationSource,
                content = {
                    viewModel.state.value.spots.forEach { spot ->
                        Marker(
                            state = MarkerState(LatLng(spot.lat ?: 0.0, spot.lng ?: 0.0)).apply {
                              if (state.isSnippetVisible) this.showInfoWindow() else this.hideInfoWindow()
                            },
                            title = "${spot.type} (${spot.lat}, ${spot.lng})",
                            snippet = "Short click to delete, \n Long Click to Open Control",
                            onInfoWindowClick = {
                                viewModel.onEvent(
                                    MapEvent.OnInfoWindowClick(spot)
                                )
                            },
                            onInfoWindowLongClick = {
                                viewModel.onEvent(
                                    MapEvent.OnInfoWindowLongClick(spot)
                                )
                            },
                            onClick = {
                                it.showInfoWindow()
                                viewModel.onEvent(
                                    MapEvent.OnCarSpotMarkerClick
                                )
                                true
                            },
                            icon = when (spot.type) {
                                MarkerType.PARKING_SPOT -> {
                                    bitmapDescriptorFromVector(localContext, R.drawable.ic_baseline_local_parking_24)
                                }
                                MarkerType.CAR_SPOT -> {
                                    BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_GREEN
                                    )
                                }
                                null -> {
                                    BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_GREEN
                                    )
                                }
                            }
                        )
                    }

                    viewModel.state.value.parkingLots.forEach { spot ->
                        Marker(
                            state = MarkerState(LatLng(spot.lat ?: 0.0, spot.lng ?: 0.0)),
                            title = "${spot.type} (${spot.lat}, ${spot.lng})",
                            snippet = "Short click to delete, \n Long Click to Open Control",
                            onInfoWindowClick = {
//                                viewModel.onEvent(
//                                     MapEvent.OnInfoWindowClick(spot)
//                                )
                            },
                            onInfoWindowLongClick = {
                                viewModel.onEvent(
                                    MapEvent.OnInfoWindowLongClick(spot)
                                )
                            },
                            onClick = {
                                it.showInfoWindow()
                                true
                            },
                            icon = bitmapDescriptorFromVector(localContext, R.drawable.ic_baseline_local_parking_24)
                        )
                    }

                    if (state.isAlertDialogDisplayed) {

                        RemoveFromDbDialog(
                            onConfirmButtonClick = {
                                // remove from map and db
                                viewModel.onEvent(MapEvent.RemoveMarkerFromDb(state.spotToBeDeleted))
                            },
                            onDismissButtonClick = {
                                viewModel.onEvent(MapEvent.OnDismissRemoveMarkerFromDbClick)
                            }
                        )
                    }

                    if (state.isMarkerControlDialogDisplayed) {
                        MarkerControlDialog(
                            spot = state.spotToBeControlled,
                            isSaved = state.spotToBeControlled.isSaved ?: false,
                            onRemoveBtnClick = {
                                viewModel.onEvent(MapEvent.RemoveMarkerFromDb(state.spotToBeControlled))
                            },
                            onSaveBtnClick = {
                                viewModel.onEvent(MapEvent.OnSaveMarkerBtnClick(state.spotToBeControlled))
                            },
                            onGetRouteBtnClick = {
                                viewModel.onEvent(MapEvent.OnGetRouteBtnClick("${state.currentLocation?.latitude ?: 0.0},${state.currentLocation?.longitude ?: 0.0}", "${state.spotToBeControlled.lat},${state.spotToBeControlled.lng}"))
                            },
                            onDismissDialog = {
                                viewModel.onEvent(MapEvent.OnDismissMarkerControllDialog)
                            }
                        )
                    }

                    state.path.forEach { latLng ->
                        drawPolylines += latLng
                    }
                    Polyline(
                        points = drawPolylines,
                        color = Color.Red,
                        width = 10f
                    )
                }
            )

            if (!state.isMapLoaded) {
                AnimatedVisibility(
                    modifier = Modifier
                        .matchParentSize(),
                    visible = !state.isMapLoaded,
                    enter = EnterTransition.None,
                    exit = fadeOut()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .background(MaterialTheme.colors.background)
                            .wrapContentSize()
                    )
                }
            }
            if (state.isInShowRouteState) {
                Row(
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Button(
                        onClick = {
                            viewModel.onEvent(MapEvent.ToggleLocationTrackingService(localContext))
                        },
                    ) {
                        Text(text = if (state.isInTrackingRouteState) "Stop" else "Start")
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    IconButton(onClick = {
                        viewModel.onEvent(MapEvent.ToggleShowRouteState(localContext))
                    }) {
                        Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_close_24), contentDescription = "close")
                    }
                }
            }

            if (!state.isInShowRouteState) {
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
                                    viewModel.onEvent(MapEvent.OnZoomOutClick(cameraPositionState))
                                },
                                onZoomIn = {
                                    viewModel.onEvent(MapEvent.OnZoomInClick(cameraPositionState))
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
                        MarkerToggles(
                            onShowParkingSpotsToggleClick = { viewModel.onEvent(MapEvent.OnShowParkingSpotsToggleClick) },
                            onHideParkingSpotsToggleClick = { viewModel.onEvent(MapEvent.OnHideParkingSpotsToggleClick) },
                            onShowCarSpotsToggleClick = { viewModel.onEvent(MapEvent.OnShowCarSpotsToggleClick) },
                            onHideCarSpotsToggleClick = { viewModel.onEvent(MapEvent.OnHideCarSpotsToggleClick) }
                        )
                    }
                }
            }
        }
    } else {
        LaunchedEffect(multiplePermissionResultLauncher) {
            multiplePermissionResultLauncher.launch(permissionsToRequest)
        }
    }

    state.visiblePermissionDialogQueue
        .reversed()
        .forEach { permission ->
                CustomPermissionDialog(
                    permissionTextProvider = when (permission) {
                        Manifest.permission.ACCESS_FINE_LOCATION -> {
                            FineLocationPermissionTextProvider()
                        }
                        Manifest.permission.ACCESS_COARSE_LOCATION -> {
                            CoarseLocationPermissionTextProvider()
                        }
                        else -> return@forEach
                    },
                    isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                        localContext.findActivity(), permission
                    ),
//                    !shouldShowRequestPermissionRationale(
//                        localContext as Activity, permission
//                    ),
                    onDismiss = { viewModel.onEvent(MapEvent.OnPermissionDialogDismiss) },
                    onOkClick = {
                        viewModel.onEvent(MapEvent.OnPermissionDialogDismiss)
                        multiplePermissionResultLauncher.launch(
                            arrayOf(permission)
                        )
                    },
                    onGoToAppSettingsClick = {
                        val i = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", localContext.packageName, null)
                        )
                        localContext.startActivity(i)
                    }
                )
        }
}

/**
 * A [LocationSource] which allows it's location to be set. In practice you would request location
 * updates (https://developer.android.com/training/location/request-updates).
 */
class MyLocationSource : LocationSource {

    private var listener: LocationSource.OnLocationChangedListener? = null

    override fun activate(listener: LocationSource.OnLocationChangedListener) {
        this.listener = listener
    }

    override fun deactivate() {
        listener = null
    }

    fun onLocationChanged(location: Location) {
        listener?.onLocationChanged(location)
    }
}
