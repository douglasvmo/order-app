package com.example.simpleorderapplication.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.simpleorderapplication.ui.orderlist.OrderListScreen
import com.example.simpleorderapplication.ui.orderproducts.OrderProductsScreen
import com.example.simpleorderapplication.ui.theme.SimpleOrderApplicationTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SimpleOrderApplicationTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "orders") {
                        composable("orders") {
                            OrderListScreen(navController)

                        }
                        composable(
                            "orders/{orderId}/products",
                            arguments = listOf(navArgument("orderId"){ type = NavType.StringType})
                        ) {
                            OrderProductsScreen(
                                navController,
                                orderId = it.arguments?.getString("orderId") ?: ""
                            )
                        }
                    }
                }

            }
        }
    }
}