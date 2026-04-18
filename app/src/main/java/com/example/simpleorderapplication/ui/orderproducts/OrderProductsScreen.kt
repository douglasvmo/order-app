package com.example.simpleorderapplication.ui.orderproducts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
        val order = viewModel.getUnmanagedOrder();
        if(order != null) {
            val file = PdfUtils.getPdfFromOrder(context, order);
            ShereUtils.sherePdfWithWhatsapp(context, file)
        }
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
            ExtendedFloatingActionButton (
                onClick = {
                    viewModel.dispatch(OrderProductIntent.ToggleModal)
                },
                icon = {Icon(Icons.Default.Add, "Novo Item")},
                text = {Text("Novo Item")}
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
                    text = product.description,
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