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
import com.example.parkmycar.core.components.map.GoogleMapView
import com.example.parkmycar.core.components.permission.CoarseLocationPermissionTextProvider
import com.example.parkmycar.core.components.permission.CustomPermissionDialog
import com.example.parkmycar.core.components.permission.FineLocationPermissionTextProvider
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.collectLatest

val singapore = LatLng(1.3588227, 103.8742114)
val singapore2 = LatLng(1.40, 103.77)
val singapore3 = LatLng(1.45, 103.77)
val defaultCameraPosition = CameraPosition.fromLatLngZoom(singapore, 11f)


private val permissionsToRequest = arrayOf(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION,
)

fun AppCompatActivity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also {
        startActivity(it)
    }
}

fun Context.getActivity(): AppCompatActivity? = when (this) {
    is AppCompatActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
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
//    LaunchedEffect(snackbarHostState) {
//        viewModel.eventFlow.collectLatest { event ->
//            when(event) {
//                is MapUiEvent.LaunchPermissionLauncher -> {
//                    Log.d(TAG, "MapScreen: event.message launcher " + event.message)
//                    snackbarHostState.showSnackbar(event.message, "asdasd", SnackbarDuration.Long)
//                    multiplePermissionResultLauncher.launch(permissionsToRequest)
//                }
//                is MapUiEvent.ShowSnackbar -> {
//                    //todo
//                }
////                is MapUiEvent.MapIsLoaded -> {
////                    viewModel.onEvent(MapEvent.MapLoaded)
////                }
//            }
//        }
//    }


    if (state.permissionsGranted) {
        //var isMapLoaded by remember { mutableStateOf(state.isMapLoaded) }
        //var isMapLoaded by remember { mutableStateOf(false) }
        // Observing and controlling the camera's state can be done with a CameraPositionState
        val cameraPositionState = rememberCameraPositionState {
            position = defaultCameraPosition
        }

        Box(Modifier.fillMaxSize()) {
            GoogleMapView(
                modifier = Modifier.matchParentSize(),
                cameraPositionState = cameraPositionState,
                onMapLoaded = {
                    viewModel.onEvent(MapEvent.MapLoaded)
                },
                viewModel = viewModel
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
//                    isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
//                        context.getActivity()!!.parent, permission
//                    ),
                    isPermanentlyDeclined = localContext.getActivity()?.let {
                        !shouldShowRequestPermissionRationale(
                            it.parent, permission
                        )
                    } ?: false,
                    onDismiss = { viewModel.onEvent(MapEvent.OnPermissionDialogDismiss) },
                    onOkClick = {
                        viewModel.onEvent(MapEvent.OnPermissionDialogDismiss)

                        multiplePermissionResultLauncher.launch(
                            arrayOf(permission)
                        )

                    },
                    onGoToAppSettingsClick = {
                        // on below line we are opening our intent
                        // for wireless settings screen.
                        val i = Intent(ACTION_WIRELESS_SETTINGS)
                        localContext.startActivity(i)
                    }
                )

        }




//    if (ActivityCompat.checkSelfPermission(
//            localContext,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//            localContext,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED
//    ) {

//    } else {
//        //
//    }
}