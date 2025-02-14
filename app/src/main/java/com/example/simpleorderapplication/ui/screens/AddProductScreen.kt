package com.example.simpleorderapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.simpleorderapplication.R
import com.example.simpleorderapplication.ui.components.SimpleOrderAppTopBar
import com.example.simpleorderapplication.ui.viewmodels.OrderViewModel

@Composable
fun AddProductScreen(
    navController: NavController,
    viewModel: OrderViewModel
) {
    val navController = rememberNavController()

    Scaffold (
        topBar = {
            SimpleOrderAppTopBar(
                stringResource(R.string.order_screen_title).plus(" "),
                onGoBackClick = { navController.popBackStack() }
            )
        }
    ){ innerPadding ->
        Column(Modifier.padding(innerPadding)) {  }

    }
}