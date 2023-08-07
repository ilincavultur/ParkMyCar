package com.example.parkmycar.core.components.map

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MarkerToggles(
    onCarSpotsCheckedChange: (Boolean) -> Unit,
    onParkingSpotsCheckedChange: (Boolean) -> Unit,
) {
    Row{
        SwitchButton(
            onCheckedChange = onParkingSpotsCheckedChange,
            checkedTrackColor = Color(0xFF0029FF),
            uncheckedTrackColor = Color(0x3E0029FF)
        )
        SwitchButton(
            onCheckedChange = onCarSpotsCheckedChange,
            checkedTrackColor = Color(0xFF673AB7),
            uncheckedTrackColor = Color(0x3D673AB7)
        )
    }
}