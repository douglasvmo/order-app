package com.example.simpleorderapplication.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simpleorderapplication.ui.screens.CreateOrderScreen
import com.example.simpleorderapplication.ui.screens.OrderListScreen
import com.example.simpleorderapplication.ui.theme.SimpleOrderApplicationTheme
import com.example.simpleorderapplication.ui.viewmodels.OrderViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val viewModel: OrderViewModel = viewModel(factory = OrderViewModel.OrderViewModelFactory(this))
            SimpleOrderApplicationTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = AppScreen.OrderList.name) {
                        composable(AppScreen.OrderList.name) {
                            OrderListScreen(
                                viewModel,
                                onNewOrderClick = {
                                    navController.navigate(AppScreen.CreateOrder.name)
                                }
                            )
                        }
                        composable(AppScreen.CreateOrder.name) {
                            CreateOrderScreen(
                                viewModel,
                                onBackClick = {
                                    navController.navigate(AppScreen.OrderList.name)
                                }
                            )
                        }
                    }
                }

            }
        }
    }
}