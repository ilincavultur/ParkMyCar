package com.example.parkmycar.core.components.card

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.parkmycar.feature_map.domain.models.Spot

@Composable
fun RowScope.SpotCard(
    //spot: Spot
) {
    Text(
        text = "Title",
        style = TextStyle(
            color = Color.DarkGray
        ),
        modifier = Modifier.weight(0.75f)
    )

    Box(
        modifier = Modifier
            .size(70.dp, 70.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {

    }

}