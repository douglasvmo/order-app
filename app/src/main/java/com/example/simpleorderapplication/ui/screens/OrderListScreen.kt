package com.example.simpleorderapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simpleorderapplication.R
import com.example.simpleorderapplication.data.models.Product
import com.example.simpleorderapplication.data.relations.OrderWithClient
import com.example.simpleorderapplication.ui.components.SimpleOrderAppTopBar
import com.example.simpleorderapplication.ui.viewmodels.OrderViewModel


@Composable
fun OrderListScreen(
    viewModel: OrderViewModel,
    onOrderClick: () -> Unit = {},
    onNewOrderClick: () -> Unit = {}
) {
    var presses by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.loadOrders()
    }


    Scaffold(
        topBar = {
            SimpleOrderAppTopBar(stringResource(R.string.order_list_screen_title))
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewOrderClick,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding), contentPadding = PaddingValues(4.dp)) {
            viewModel.orders.value?.let {
                items(viewModel.orders.value!!) {
                        order -> OrderCard(order)
                }
            }
        }
    }

}

@Composable
fun OrderCard(order: OrderWithClient){
    OutlinedCard(
        Modifier.fillMaxWidth().padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Row{
            Text(stringResource(R.string.order_card_client_name), Modifier.padding(horizontal = 6.dp))
            Text(order.client.name)
        }
        Row() {
            Text(stringResource(R.string.order_card_client_phone), Modifier.padding(horizontal = 6.dp))
            Text(order.client.phone)
        }
        Row() {
            Text(stringResource(R.string.order_card_order_price), Modifier.padding(horizontal = 6.dp))
            Text(order.order.total.toString())
        }





    }

}



@Composable
fun ProductCard(product: Product){
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Row(modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp, 4.dp)) {
                Text(stringResource(R.string.product_card_quant), fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleSmall.fontSize)
                Text(product.quantity.toString())
            }
            Column(modifier = Modifier.padding(4.dp)) {
                Text(stringResource(R.string.product_card_description), fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleSmall.fontSize)
                Text(product.description.toString())
            }
            Column(modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.End) {
                Text(stringResource(R.string.product_card_price), fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleSmall.fontSize)
                Text(product.price.toString())
            }
        }
    }
}


@Composable
@Preview
fun OrderListScreenPreview() {

}