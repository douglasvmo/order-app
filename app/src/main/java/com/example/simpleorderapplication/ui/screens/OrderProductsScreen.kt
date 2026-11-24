package com.example.simpleorderapplication.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simpleorderapplication.R
import com.example.simpleorderapplication.domain.Order
import com.example.simpleorderapplication.domain.Product
import com.example.simpleorderapplication.ui.components.CreateProductBottomSheet
import com.example.simpleorderapplication.ui.components.GoBackIcon
import com.example.simpleorderapplication.ui.components.SimpleOrderAppTopBar
import com.example.simpleorderapplication.ui.order.OrderAction
import com.example.simpleorderapplication.ui.order.OrderState

@Composable
fun OrderProductsScreen(
  state: OrderState,
  onAction: (OrderAction) -> Unit
) {

    var showModal by remember { mutableStateOf(false) }


    Scaffold (
        topBar = {
            SimpleOrderAppTopBar(
                   stringResource(R.string.order_screen_title).plus(state.currentOrder?.code.toString()),
                navegationIcon = { GoBackIcon {onAction(OrderAction.GoBackOrders) } },
                actions = {
                    IconButton(onClick = {onAction(OrderAction.Shere)}) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton (
                onClick = {
                    showModal = true
                },
                icon = {Icon(Icons.Default.Add, "Novo Item")},
                text = {Text("Novo Item")}
            )

        }
    ){ innerPadding ->
        LazyColumn(Modifier.padding(innerPadding), contentPadding = PaddingValues(4.dp)) {
            if(state.currentOrder !== null) {
                items(state.currentOrder.products) { product ->
                    ProductCard(product)
                }
            }
        }

        if(showModal){
            CreateProductBottomSheet(
                onDismiss = { showModal = false },
                onCreateProduct = { description, quantity, price ->
                    onAction(OrderAction.AddProduct(description, price, quantity))
                }
            )
        }

    }
}

@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Quantidade (badge)
            Box(
                modifier = Modifier
                    .size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = product.quantity.toInt().toString(),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Descrição (principal)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.description.trim(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Preço
            Text(
                text = "R$ %.2f".format(product.priceCents / 100.0),
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )
        }
    }
}

@Preview
@Composable
fun OrderProductListPreview() {
    val state = OrderState(
        currentOrder = Order(
            id = "order-preview",
            code = "10",
            clientName = "Cliente",
            clientCode = null,
            clientPhone = null,
            products = listOf(
                Product("product-1", "Parteleira MDF", 2.0, 5000),
                Product("product-2", "Mesa trabalho em MDF", 1.0, 100000)
            )
        )
    )

    OrderProductsScreen(
        state,
        {}
    )
}
