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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.simpleorderapplication.R
import com.example.simpleorderapplication.data.models.OrderEntity

import com.example.simpleorderapplication.ui.AppScreen
import com.example.simpleorderapplication.ui.components.SimpleOrderAppTopBar
import com.example.simpleorderapplication.ui.viewmodels.OrderViewModel
import com.example.simpleorderapplication.utils.Formater


@Composable
fun OrderListScreen(
    navController: NavController,
    viewModel: OrderViewModel
) {
    var orders by remember { mutableStateOf<List<OrderEntity>>(emptyList()) }
    var presses by remember { mutableIntStateOf(0) }


    LaunchedEffect(Unit) {
       orders = viewModel.getOrders()
    }


    Scaffold(
        topBar = {
            SimpleOrderAppTopBar(stringResource(R.string.order_list_screen_title))
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {navController.navigate(AppScreen.CreateOrder.withArgs(0))},
                icon = {Icon(Icons.Default.Add, "Novo Pedido")},
                text = { Text( "Novo Pedido") },
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(4.dp)
        ) {
            items(orders) { order ->
                OrderCard(
                    order,
                    onClick = {
                        navController.navigate(AppScreen.OrderDetails.withArgs(order.id))
                    },
                )
            }

        }
    }


}

@Composable
fun OrderCard(order: OrderEntity, onClick: () -> Unit) {
    ElevatedCard(
        Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple()
            ) { onClick() },
        elevation = CardDefaults.cardElevation( 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Row(Modifier.padding(4.dp)) {
            Column(Modifier.fillMaxWidth(0.85f).padding(10.dp)) {
                Row( verticalAlignment = Alignment.Bottom) {
                    Text(stringResource(R.string.order_card_order_number))
                    Text(order.id.toString(), Modifier.padding(horizontal = 4.dp), fontSize = 16.sp)
                }
                Row {
                    Text(stringResource(R.string.order_card_client_name))
                    Text(order.clientName, Modifier.padding(horizontal = 4.dp), fontSize = 16.sp)
                }
                Row {
                    Text(stringResource(R.string.order_card_date))
                    Text(Formater.dateToString(order.date), Modifier.padding(horizontal = 4.dp))
                }
            }
            Column(Modifier.fillMaxWidth(), Arrangement.Top, Alignment.CenterHorizontally) {
                Text(order.amount.div(100).toString(), fontSize = 12.sp)
            }
        }

    }
}

