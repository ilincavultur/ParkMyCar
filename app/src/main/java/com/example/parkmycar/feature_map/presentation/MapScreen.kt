package com.example.parkmycar.feature_map.presentation

import android.Manifest
import android.app.Activity
import android.app.LocalActivityManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.provider.Settings.ACTION_WIRELESS_SETTINGS
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.parkmycar.MainActivity
import com.example.parkmycar.R
import com.example.parkmycar.core.components.map.GoogleMapView
import com.example.parkmycar.core.components.map.MarkerControlDialog
import com.example.parkmycar.core.components.map.RemoveFromDbDialog
import com.example.parkmycar.core.components.map.bitmapDescriptorFromVector
import com.example.parkmycar.core.components.permission.CoarseLocationPermissionTextProvider
import com.example.parkmycar.core.components.permission.CustomPermissionDialog
import com.example.parkmycar.core.components.permission.FineLocationPermissionTextProvider
import com.example.parkmycar.feature_map.domain.models.MarkerType
import com.example.parkmycar.feature_map.domain.models.Spot
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collectLatest




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

    val cameraPositionState = rememberCameraPositionState {
        position = state.defaultCameraPosition
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
        //var isMapLoaded by remember { mutableStateOf(state.isMapLoaded) }
        //var isMapLoaded by remember { mutableStateOf(false) }
        // Observing and controlling the camera's state can be done with a CameraPositionState
//        val cameraPositionState = rememberCameraPositionState {
//            position = defaultCameraPosition
//        }

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
                onZoomOutClick = {
                    viewModel.onEvent(MapEvent.OnZoomOutClick(cameraPositionState))
                },
                onZoomInClick = {
                    viewModel.onEvent(MapEvent.OnZoomInClick(cameraPositionState))
                },
                onSearchIconClick = {
                    viewModel.onEvent(MapEvent.OnSearchButtonClick)
                },
                onShowParkingSpotsToggleClick = {
                    viewModel.onEvent(MapEvent.OnShowParkingSpotsToggleClick)
                },
                onHideParkingSpotsToggleClick = {
                    viewModel.onEvent(MapEvent.OnHideParkingSpotsToggleClick)
                },
                onShowCarSpotsToggleClick = {
                    viewModel.onEvent(MapEvent.OnShowCarSpotsToggleClick)
                },
                onHideCarSpotsToggleClick = {
                    viewModel.onEvent(MapEvent.OnHideCarSpotsToggleClick)
                },
                content = {
                    viewModel.state.value.spots.forEach { spot ->
                        Marker(
                            state = MarkerState(LatLng(spot.lat ?: 0.0, spot.lng ?: 0.0)),
                            title = "Parking spot (${spot.lat}, ${spot.lng})",
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
                            title = "Parking spot (${spot.lat}, ${spot.lng})",
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

                            },
                            onDismissDialog = {
                                viewModel.onEvent(MapEvent.OnDismissMarkerControllDialog)
                            }
                        )
                    }
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