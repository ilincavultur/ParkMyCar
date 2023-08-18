package com.example.parkmycar.core.components.map

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.MarkerToggles(
    onShowParkingSpotsToggleClick: () -> Unit,
    onHideParkingSpotsToggleClick: () -> Unit,
    onShowCarSpotsToggleClick: () -> Unit,
    onHideCarSpotsToggleClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.Start,
    ) {
        SwitchButton(
            onCheckedChange = {
                if (it) {
                    onShowParkingSpotsToggleClick()
                } else {
                    onHideParkingSpotsToggleClick()
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
                    onShowCarSpotsToggleClick()
                } else {
                    onHideCarSpotsToggleClick()
                }
            },
            checkedTrackColor = Color(0xFF5EE913),
            uncheckedTrackColor = Color(0x3D5EE913)
        )
    }
}