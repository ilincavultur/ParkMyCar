package com.example.parkmycar.core.components.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.parkmycar.ui.theme.PurplePrimary

@Composable
fun MapButton(text: String, onClick: () -> Unit, icon: ImageVector) {
    IconButton(
        modifier = Modifier.padding(4.dp),
        onClick = onClick
    ) {
        Icon(imageVector = icon, contentDescription = text, modifier = Modifier.size(40.dp), tint = PurplePrimary)
    }
}