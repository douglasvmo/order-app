package com.example.simpleorderapplication.ui.orderlist

import CreateOrderBottomSheet
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.simpleorderapplication.R
import com.example.simpleorderapplication.ui.components.OrderCard
import com.example.simpleorderapplication.ui.components.SimpleOrderAppTopBar
import org.koin.androidx.compose.koinViewModel


@Composable
fun OrderListScreen(
    navController: NavController,
    viewModel: OrderListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    var showModal by remember { mutableStateOf(false) }


    if (showModal) {
        CreateOrderBottomSheet(
            onDismiss = { showModal = false },
            onCreateOrder = { clientName, clientPhone, clientCPF ->
                viewModel.dispath(OrderListIntent.CreateOrder(clientName, clientPhone, clientCPF))
            }
        )
    }


    Scaffold(
        topBar = {
            SimpleOrderAppTopBar(stringResource(R.string.order_list_screen_title))
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    showModal = true
                },
                icon = { Icon(Icons.Default.Add, "Novo Pedido") },
                text = { Text("Novo Pedido") },
            )
        }
    ) { paddingValues ->
        LazyColumn(Modifier.padding(paddingValues)) {
            if(state.erro !== null) {
                item {
                    Text(
                        text = state.erro!!,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }

            if(state.isLoading) {
                item {
                    Text(
                        text = "Carregando...",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }

            items(state.orders) { order ->
                OrderCard(order) {
                    viewModel.dispath(OrderListIntent.OrderSelect(order.id.toString()))
                }
            }

        }

        LaunchedEffect(Unit) {
            viewModel.dispath(OrderListIntent.LoadOrders)
        }

        LaunchedEffect(Unit) {
            viewModel.effect.collect { effect ->
                when (effect){
                    is OrderListEffect.NavigateToOrderProducts -> navController.navigate("orders/${effect.orderId}/products")
                }
            }
        }


    }
}
