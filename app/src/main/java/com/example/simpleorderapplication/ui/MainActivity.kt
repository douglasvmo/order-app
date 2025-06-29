package com.example.simpleorderapplication.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.simpleorderapplication.ui.screens.AddProductScreen
import com.example.simpleorderapplication.ui.screens.ProductListScreen
import com.example.simpleorderapplication.ui.screens.CreateOrderScreen
import com.example.simpleorderapplication.ui.screens.OrderListScreen
import com.example.simpleorderapplication.ui.theme.SimpleOrderApplicationTheme
import com.example.simpleorderapplication.ui.viewmodels.OrderViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val viewModel: OrderViewModel = viewModel(factory = OrderViewModel.OrderViewModelFactory(this.applicationContext))
            SimpleOrderApplicationTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = AppScreen.OrderList.route) {
                        composable(AppScreen.OrderList.route) {
                            OrderListScreen(navController, viewModel)
                        }
                        composable(AppScreen.CreateOrder.route) {
                            CreateOrderScreen(navController, viewModel)
                        }
                        composable(AppScreen.OrderDetails.route,  arguments = listOf(navArgument("orderId") { type = NavType.LongType })) {
                            ProductListScreen(navController, viewModel, it.arguments!!.getLong("orderId"))
                        }
                        composable(
                            AppScreen.AddProduct.route,
                            arguments = listOf(navArgument("orderId") { type = NavType.LongType })
                        ) {
                            AddProductScreen(navController, viewModel, it.arguments!!.getLong("orderId"))
                        }
                    }
                }

            }
        }
    }
}