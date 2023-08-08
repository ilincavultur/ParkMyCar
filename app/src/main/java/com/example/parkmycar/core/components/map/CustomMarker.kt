package com.example.parkmycar.core.components.map

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.example.parkmycar.R
import com.example.parkmycar.feature_map.domain.models.MarkerType
import com.example.parkmycar.feature_map.domain.models.Spot
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState

fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int
): BitmapDescriptor? {

    // retrieve the actual drawable
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    // draw it onto the bitmap
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}

@Composable
fun CustomMarker(
    context: Context,
    state: MarkerState,
    @DrawableRes iconResourceId: Int,
    markerClick: (Marker) -> Boolean,
    type: MarkerType,
    isSaved: Boolean,
    onRemoveParkingSpot: () -> Unit,
    onRemoveCarSpot: () -> Unit,
    onSaveParkingSpot: () -> Unit,
    onGetRoute: () -> Unit,
) {
    MarkerInfoWindowContent(
        state = state,
        //title = title,
        icon = bitmapDescriptorFromVector(context = context, vectorResId = iconResourceId),
        onClick = markerClick,
        onInfoWindowClick = {
            Log.d(TAG, "CustomMarker: i clicked !")
        }
    ) {

//        if (it.isInfoWindowShown) {
//            it.hideInfoWindow()
//        } else {
//            it.showInfoWindow()
//        }


        Button(onClick = onRemoveParkingSpot) {
            // remove
            Text(text = "remove")
        }
//        Surface {
//            Column {
//                when (type) {
//                    MarkerType.PARKING_SPOT -> {
//                        if (isSaved) {
//                            Button(onClick = onRemoveParkingSpot) {
//                                // remove
//                                Text(text = "remove")
//                            }
//
//                        } else {
//                            Button(onClick =  onSaveParkingSpot) {
//                                // save
//                                Text(text = "save")
//                            }
//                        }
//                        Button(onClick = onGetRoute) {
//                            // get route
//                            Text(text = "route")
//                        }
//                    }
//                    MarkerType.CAR_SPOT -> {
//                        Button(onClick = onRemoveCarSpot) {
//                            // remove
//                            Text(text = "remove")
//                        }
//                        Button(onClick = onGetRoute) {
//                            // get route
//                            Text(text = "route")
//                        }
//                    }
//                }
//            }
//        }
    }
}