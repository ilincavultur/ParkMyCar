package com.example.parkmycar.core.components.map

import androidx.compose.foundation.layout.*

import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Maximize
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ZoomControls(
    onZoomOut: () -> Unit,
    onZoomIn: () -> Unit,
) {
    Row {
        MapButton("", onClick = { onZoomOut() }, icon = Icons.Default.Remove)
        Spacer(Modifier.size(24.dp))
        MapButton("", onClick = { onZoomIn() }, icon = Icons.Default.Add)
    }
}