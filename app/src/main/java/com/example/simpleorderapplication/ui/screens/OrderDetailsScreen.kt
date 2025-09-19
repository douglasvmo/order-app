package com.example.simpleorderapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.simpleorderapplication.R
import com.example.simpleorderapplication.domain.Order
import com.example.simpleorderapplication.domain.Product
import com.example.simpleorderapplication.ui.AppScreen
import com.example.simpleorderapplication.ui.components.FabSpeedDial
import com.example.simpleorderapplication.ui.components.GoBackIcon
import com.example.simpleorderapplication.ui.components.SimpleOrderAppTopBar
import com.example.simpleorderapplication.ui.viewmodels.OrderViewModel
import com.example.simpleorderapplication.utils.PdfUtils
import com.example.simpleorderapplication.utils.ShereUtils

@Composable
fun ProductListScreen(
    navController: NavController,
    viewModel: OrderViewModel,
    orderId: Long
) {
    val context = LocalContext.current
    var order by remember { mutableStateOf<Order>(Order()) }


    LaunchedEffect(Unit) {
        order = viewModel.getOrder(orderId)
    }

    fun toShere() {
        val file = PdfUtils.getPdfFromOrder(context, order);
        ShereUtils.sherePdfWithWhatsapp(context, file)
    }

    Scaffold (
        topBar = {
            SimpleOrderAppTopBar(
                   stringResource(R.string.order_screen_title).plus(orderId),
                navegationIcon = { GoBackIcon { navController.popBackStack() } },
                actions = {
                    IconButton(onClick = {toShere()}) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FabSpeedDial(
                onClickEdit = {
                    val path = AppScreen.CreateOrder.withArgs(orderId)
                    navController.navigate(path)
                },
                onClickShare = {
                    toShere()
                },
                onClickAdd = {
                    val path = AppScreen.AddProduct.withArgs(orderId)
                    navController.navigate(path)
                }
            )
        }
    ){ innerPadding ->
        LazyColumn(Modifier.padding(innerPadding), contentPadding = PaddingValues(4.dp)) {
            items(order.products) {
               product -> ProductCard(product)

            }

        }

    }
}

@Composable
fun ProductCard(product: Product){
    Card(
        Modifier.padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        )
    ) {
        Row(modifier = Modifier
            .padding(4.dp)
            .fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.padding(14.dp, 4.dp).fillMaxWidth(0.08f)) {
                Text(stringResource(R.string.product_card_quant), fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleMedium.fontSize)
                Text(product.quantity.toString(), fontSize = MaterialTheme.typography.bodyLarge.fontSize)
            }
            Column(modifier = Modifier
                .padding(4.dp)
                .fillMaxSize(0.7f)) {
                Text(stringResource(R.string.product_card_description), fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleMedium.fontSize)
                Text(product.description, fontSize = MaterialTheme.typography.bodyLarge.fontSize, maxLines = 1, overflow = TextOverflow.Clip)
            }
            Column(modifier = Modifier
                .padding(4.dp)
            ) {
                Text(stringResource(R.string.product_card_price), fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleMedium.fontSize)
                Text("R$ %.2f".format(product.price.div(100.0)), fontSize = MaterialTheme.typography.bodyLarge.fontSize)
            }
        }
    }
}