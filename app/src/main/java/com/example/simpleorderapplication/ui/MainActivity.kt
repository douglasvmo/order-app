package com.example.simpleorderapplication.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.simpleorderapplication.ui.order.OrderEffect
import com.example.simpleorderapplication.ui.order.OrderViewModel
import com.example.simpleorderapplication.ui.screens.OrderListScreen
import com.example.simpleorderapplication.ui.screens.OrderProductsScreen
import com.example.simpleorderapplication.ui.theme.SimpleOrderApplicationTheme
import com.example.simpleorderapplication.utils.PdfUtils
import com.example.simpleorderapplication.utils.ShereUtils
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        setContent {
            SimpleOrderApplicationTheme {
                Surface {
                    val navController = rememberNavController()
                    val orderViewModel: OrderViewModel = koinViewModel()
                    val state  by orderViewModel.state.collectAsState()

                    NavHost(navController, startDestination = "orders") {
                        composable("orders") {

                            OrderListScreen(
                                state,
                                onAction = orderViewModel::dispatch
                            )

                            LaunchedEffect(Unit){

                                orderViewModel.effect.collect { effect ->
                                    when(effect){
                                        is OrderEffect.NavigateToOrder -> {
                                            navController.navigate("orders/${effect.orderId}/products")
                                        }
                                        else -> null
                                    }
                                }
                            }

                        }
                        composable(
                            "orders/{orderId}/products",
                            arguments = listOf(navArgument("orderId"){ type = NavType.StringType})
                        ) {
                            val context = LocalContext.current

                            OrderProductsScreen(
                                state,
                                orderViewModel::dispatch
                            )

                            LaunchedEffect(Unit) {
                                orderViewModel.effect.collect { effect ->
                                    when(effect){
                                        is OrderEffect.GoBackOrders -> navController.popBackStack()
                                        is OrderEffect.ShereOrder -> {
                                            val document = PdfUtils.getPdfFromOrder(context, effect.detachedOrder)
                                            ShereUtils.sherePdfWithWhatsapp(context, document)
                                        }
                                        else -> null
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}