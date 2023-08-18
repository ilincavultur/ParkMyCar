package com.example.parkmycar.core.components.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.parkmycar.feature_map.domain.models.MarkerType
import com.example.parkmycar.feature_map.domain.models.Spot
import com.example.parkmycar.ui.theme.PurplePrimary

@Composable
fun MarkerControlDialog(
    spot: Spot,
    isSaved: Boolean,
    onRemoveBtnClick: () -> Unit,
    onSaveBtnClick: () -> Unit,
    onGetRouteBtnClick: () -> Unit,
    onDismissDialog: () -> Unit
) {
    Dialog(onDismissRequest = {
        onDismissDialog()
    }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row {
                        Text(
                            spot.type?.name ?: "",
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp
                        )
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Row {
                        Column {
                            Text(
                                "Lat: " + spot.lat,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp
                            )

                            Spacer(modifier = Modifier.size(20.dp))

                            Text(
                                "Lng: " + spot.lng,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Row {
                        Button(
                            onClick = {
                                if (isSaved) {
                                    onRemoveBtnClick()
                                } else {
                                    onSaveBtnClick()
                                }
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = PurplePrimary)
                                //.padding(10.dp)
                        ) {
                            Text(text = if (isSaved) "Remove" else "Save")
                        }

                        Spacer(modifier = Modifier.size(20.dp))

                        Button(
                            onClick = {
                                onGetRouteBtnClick()
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = PurplePrimary)
                                //.padding(10.dp)
                        ) {
                            Text("Get Route")
                        }
                    }
                }
            }
        }
    }
}