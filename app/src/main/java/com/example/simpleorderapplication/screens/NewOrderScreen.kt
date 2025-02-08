package com.example.simpleorderapplication.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.simpleorderapplication.components.SimpleOrderAppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewOrderScreen() {
    Scaffold(
        topBar = { SimpleOrderAppTopBar("Novo Pedido") }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text("Field")
        }
    }
}