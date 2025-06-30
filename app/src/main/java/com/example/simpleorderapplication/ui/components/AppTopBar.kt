package com.example.simpleorderapplication.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
fun SimpleOrderAppTopBar(
    title: String = "New Screen",
    onGoBackClick: (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit) = {}
) {
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
            if (onGoBackClick != null) {
                GoBackIcon(onGoBackClick)
            }
        },
        actions = actions
    )
}

@Composable
fun GoBackIcon(onGoBackClick: () -> Unit) {
    TextButton(
        onClick = onGoBackClick
    ) {
        Icon(
            Icons.AutoMirrored.Filled.ArrowBack,
            "Go Back"
        )
    }

}
