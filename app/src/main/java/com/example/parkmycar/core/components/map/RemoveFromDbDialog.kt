package com.example.parkmycar.core.components.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.parkmycar.ui.theme.PurplePrimary
import com.example.parkmycar.ui.theme.RedPrimary

@Composable
fun RemoveFromDbDialog(
    onConfirmButtonClick: () -> Unit,
    onDismissButtonClick: () -> Unit
){
    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onCloseRequest.
        },
        title = {
            Text(text = "Are you sure you want to delete this marker from the database?")
        },
        text = {
            Text("This action cannot be undone.")
        },
        confirmButton = {
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
        },
        dismissButton = {
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
        },

    )
}