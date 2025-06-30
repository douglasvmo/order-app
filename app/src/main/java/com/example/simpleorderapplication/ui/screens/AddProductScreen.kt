package com.example.simpleorderapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.simpleorderapplication.R
import com.example.simpleorderapplication.data.models.ProductEntity
import com.example.simpleorderapplication.ui.components.SimpleOrderAppTopBar
import com.example.simpleorderapplication.ui.viewmodels.OrderViewModel
import kotlinx.coroutines.launch


@Composable
fun AddProductScreen(navController: NavController, viewModel: OrderViewModel, orderId: Long){
    val coroutineScope = rememberCoroutineScope()


    Scaffold(
        topBar = {
            SimpleOrderAppTopBar(
                stringResource(R.string.order_screen_title).plus(" ${orderId}"),
                onGoBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        ProductForm(
            Modifier.padding(innerPadding),
            onAddProduct = { quantity, description, price ->
                val product = ProductEntity(
                    quantity = quantity.toInt(),
                    description = description,
                    price = price.toLong() * 100,
                    orderId = orderId
                )

                coroutineScope.launch {
                    viewModel.addProducts(product)
                    navController.popBackStack()
                }

            },
        )
    }
}


@Composable
fun ProductForm(
    modifier: Modifier = Modifier,
    onAddProduct: (quantity: String, description: String, price: String) -> Unit,
) {
    var quantity by remember { mutableStateOf("1") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            stringResource(R.string.product_form_title),
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.titleLarge.fontSize
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text(stringResource(R.string.product_card_quant)) }
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(R.string.product_card_description)) }
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = price,
            onValueChange = { price = it },
            label = { Text(stringResource(R.string.product_card_price)) }
        )
        FilledTonalButton(
            onClick = {
                onAddProduct(quantity, description, price)
            },
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(stringResource(R.string.add_products_btn))
        }
    }
}


@Preview
@Composable
fun ScreenPreview(){
    ProductForm(
        onAddProduct = {q, d, p -> }
    )
}