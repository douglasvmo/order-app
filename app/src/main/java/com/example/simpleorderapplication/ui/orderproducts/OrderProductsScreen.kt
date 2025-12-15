package com.example.simpleorderapplication.ui.orderproducts

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.simpleorderapplication.R
import com.example.simpleorderapplication.data.models.Product
import com.example.simpleorderapplication.ui.components.CreateProductBottomSheet
import com.example.simpleorderapplication.ui.components.FabSpeedDial
import com.example.simpleorderapplication.ui.components.GoBackIcon
import com.example.simpleorderapplication.ui.components.SimpleOrderAppTopBar
import com.example.simpleorderapplication.utils.PdfUtils
import com.example.simpleorderapplication.utils.ShereUtils
import org.koin.androidx.compose.koinViewModel

@Composable
fun OrderProductsScreen(
    navController: NavController,
    viewModel: OrderProductsViewModel = koinViewModel(),
    orderId: String
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    fun toShere() {
        val file = PdfUtils.getPdfFromOrder(context, state.order!!);
        ShereUtils.sherePdfWithWhatsapp(context, file)
    }

    Scaffold (
        topBar = {
            SimpleOrderAppTopBar(
                   stringResource(R.string.order_screen_title).plus(state.order?.code.toString()),
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

                },
                onClickShare = {
                    toShere()
                },
                onClickAdd = {
                    viewModel.dispatch(OrderProductIntent.ToggleModal)
                }
            )
        }
    ){ innerPadding ->
        LazyColumn(Modifier.padding(innerPadding), contentPadding = PaddingValues(4.dp)) {
            if(state.order !== null) {
                items(state.order!!.products) { product ->
                    ProductCard(product)
                }
            }
        }

        if(state.showModal){
            CreateProductBottomSheet(
                onDismiss = { viewModel.dispatch(OrderProductIntent.ToggleModal) },
                onCreateProduct = { description, quantity, price ->
                    viewModel.dispatch(OrderProductIntent.AddProduct(description, price, quantity))
                }
            )
        }

    }

    LaunchedEffect(Unit){
        viewModel.dispatch(OrderProductIntent.LoadProducts(orderId))
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
            Column(modifier = Modifier
                .padding(14.dp, 4.dp)
                .fillMaxWidth(0.08f)) {
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
                Text("R$ %.2f".format(product.priceCents.div(100.0)), fontSize = MaterialTheme.typography.bodyLarge.fontSize)
            }
        }
    }
}