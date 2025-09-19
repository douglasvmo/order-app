package com.example.simpleorderapplication.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@Composable
fun FabSpeedDial(
    onClickAdd: () -> Unit,
    onClickShare: () -> Unit,
    onClickEdit: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(contentAlignment = Alignment.BottomEnd) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.End
        ) {

            AnimatedVisibility(visible = expanded) {
                ExtendedFloatingActionButton(
                    onClick = onClickAdd,
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                    text = { Text("Novo item") }
                )
            }

            AnimatedVisibility(visible = expanded) {
                ExtendedFloatingActionButton(
                    onClick = onClickShare,
                    icon = { Icon(Icons.Default.Share, contentDescription = "Share") },
                    text = { Text("Compartilhar") }
                )
            }

            AnimatedVisibility(visible = expanded) {
                ExtendedFloatingActionButton(
                    onClick = onClickEdit,
                    icon = { Icon(Icons.Default.Edit, contentDescription = "Edit") },
                    text = { Text("Editar pedido") },
                )
            }

            FloatingActionButton(
                onClick = { expanded = !expanded }
            ) {
                Icon(
                    if (expanded) Icons.Default.Close else Icons.Default.Add,
                    contentDescription = "Menu"
                )
            }
        }
    }
}
