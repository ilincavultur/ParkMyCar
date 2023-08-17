package com.example.parkmycar.core.components.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.parkmycar.ui.theme.PurplePrimary
import com.example.parkmycar.ui.theme.RedPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveFromDbDialog(
    onConfirmButtonClick: () -> Unit,
    onDismissButtonClick: () -> Unit
){
    // TODO: maybe instead of setting the todeletespot as a map state element, use savedstatehandle?


    androidx.compose.material3.AlertDialog(
        onDismissRequest = {
            onDismissButtonClick()
        }
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Are you sure you want to delete this marker from the database?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp
                    )

                    Spacer(modifier = Modifier.size(20.dp))

                    Text(
                        "This action cannot be undone.",
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.size(20.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                onConfirmButtonClick()
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = PurplePrimary),

                            ) {
                            Text("I am sure.")
                        }
                        Spacer(modifier = Modifier.size(20.dp))
                        Button(
                            onClick = {
                                onDismissButtonClick()
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = PurplePrimary)
                        ) {
                            Text("Keep it.")
                        }
                    }
                }
            }
        }
    }
}