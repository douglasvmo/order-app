package com.example.simpleorderapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simpleorderapplication.screens.OrderListScreen
import com.example.simpleorderapplication.screens.NewOrderScreen
import com.example.simpleorderapplication.ui.theme.SimpleOrderApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleOrderApplicationTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = AppScreen.OrderList.name) {
                        composable(AppScreen.OrderList.name) {
                            OrderListScreen(
                                onNewOrderClick = {
                                    navController.navigate(AppScreen.NewOrder.name)
                                }
                            )
                        }
                        composable(AppScreen.NewOrder.name) {
                            NewOrderScreen()
                        }
                    }
                }

            }
        }
    }
}