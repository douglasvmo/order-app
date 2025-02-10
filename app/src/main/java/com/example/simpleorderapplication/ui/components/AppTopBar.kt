package com.example.simpleorderapplication.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleOrderAppTopBar(title: String = "New Screen", onGoBackClick: (() -> Unit)? = null) {
    CenterAlignedTopAppBar(
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.primary,
            scrolledContainerColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        title = {
            Text(title)
        },
        navigationIcon = {
            if(onGoBackClick != null){
                GoBackIcon(onGoBackClick)
            }
        }
    )
}

@Composable
fun GoBackIcon(onGoBackClick: () -> Unit) {
    TextButton(
        onClick = onGoBackClick
    ) {
        Icon(
            Icons.Default.ArrowBack,
            "Go Back"
        )
    }

}
