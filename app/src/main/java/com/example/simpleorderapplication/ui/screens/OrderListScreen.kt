package com.example.simpleorderapplication.ui.screens

import CreateOrderBottomSheet
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simpleorderapplication.R
import com.example.simpleorderapplication.domain.Order
import com.example.simpleorderapplication.domain.Product
import com.example.simpleorderapplication.ui.components.OrderCard
import com.example.simpleorderapplication.ui.components.SimpleOrderAppTopBar
import com.example.simpleorderapplication.ui.order.OrderAction
import com.example.simpleorderapplication.ui.order.OrderState


@Composable
fun OrderListScreen(
    state: OrderState,
    onAction: (OrderAction) -> Unit,

    ) {

    var showModal by remember { mutableStateOf(false) }


    if (showModal) {
        CreateOrderBottomSheet(
            onDismiss = { showModal = false },
            onCreateOrder = { clientName, clientPhone, clientCPF ->  onAction(OrderAction.CreateOrder(clientName, clientPhone, clientCPF)) }
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
        LazyColumn(Modifier.padding(paddingValues), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            if(state.erro !== null) {
                item {
                    Text(
                        text = state.erro,
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
                    onAction(OrderAction.SelectOrder(order.id.toString()))
                }
            }
        }
    }
}

@Preview
@Composable
fun OrderListScreenPreview(){
    val state = OrderState(
        orders = listOf(
            Order(
                id = "order-1",
                code = "10",
                clientName = "Maria Aparecida",
                clientCode = null,
                clientPhone = null,
                products = listOf(Product("product-1", "Produto", 12.0, 20000))
            ),
            Order(
                id = "order-2",
                code = "9",
                clientName = "Joaquim Jose da Silva Xavier",
                clientCode = null,
                clientPhone = null,
                products = listOf(
                    Product("product-2", "Produto", 12.0, 20000),
                    Product("product-3", "Outro produto", 1.0, 500000)
                )
            )
        )
    )

    OrderListScreen(
        state,
        {}
    )

}
