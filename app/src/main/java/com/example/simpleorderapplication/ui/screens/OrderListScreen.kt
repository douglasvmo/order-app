package com.example.simpleorderapplication.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.ripple.rememberRipple
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.simpleorderapplication.R
import com.example.simpleorderapplication.data.relations.OrderWithClient
import com.example.simpleorderapplication.ui.AppScreen
import com.example.simpleorderapplication.ui.components.SimpleOrderAppTopBar
import com.example.simpleorderapplication.ui.viewmodels.OrderViewModel


@Composable
fun OrderListScreen(
    navController: NavController,
    viewModel: OrderViewModel
) {
    val data = viewModel.orders.observeAsState(listOf())
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
                onClick = {
                    navController.navigate(AppScreen.CreateOrder.name)
                },
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding), contentPadding = PaddingValues(4.dp)) {
                items(data.value) {
                        order -> OrderCard(
                    order,
                    onClick = {
                        viewModel.selectOrder(order)
                        navController.navigate(AppScreen.OrderDetails.name)
                    },
                )
                }

        }
    }

}

@Composable
fun OrderCard(order: OrderWithClient, onClick: () -> Unit) {
    OutlinedCard(
        Modifier
            .fillMaxWidth().padding(4.dp).clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple()
            ) { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Row(Modifier.padding(4.dp)) {
            Column(Modifier.fillMaxWidth(0.85f)) {
                Row( verticalAlignment = Alignment.Bottom) {
                    Text(stringResource(R.string.order_card_order_number))
                    Text(order.order.id.toString(), Modifier.padding(horizontal = 4.dp), fontSize = 16.sp)
                }
                Row {
                    Text(stringResource(R.string.order_card_client_name))
                    Text(order.client.name, Modifier.padding(horizontal = 4.dp), fontSize = 16.sp)
                }
                Row {
                    Text(stringResource(R.string.order_card_client_phone))
                    Text(order.client.phone, Modifier.padding(horizontal = 4.dp))
                }
            }
            Column(Modifier.fillMaxWidth(), Arrangement.Top, Alignment.CenterHorizontally) {
                Text(order.order.total.toString(), fontSize = 12.sp)
            }
        }

    }
}


@Composable
@Preview
fun OrderListScreenPreview() {

}