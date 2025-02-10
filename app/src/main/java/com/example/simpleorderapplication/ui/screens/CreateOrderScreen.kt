package com.example.simpleorderapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simpleorderapplication.R
import com.example.simpleorderapplication.data.AppDatabase
import com.example.simpleorderapplication.data.models.Client
import com.example.simpleorderapplication.data.models.Product
import com.example.simpleorderapplication.ui.components.SimpleOrderAppTopBar
import com.example.simpleorderapplication.ui.viewmodels.OrderViewModel


@Composable
fun CreateOrderScreen(
    viewModel: OrderViewModel,
    onBackClick: () -> Unit = {}
) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            SimpleOrderAppTopBar(
                stringResource(R.string.order_screen_title).plus(" ${viewModel.getNextOrderNumber()}"),
                onGoBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = CreateOrderRoute.OrderClientForm.name
            ) {
                composable(CreateOrderRoute.OrderClientForm.name) {
                    ClientForm(
                        onClickNext = { name, phone, cpf, address, email ->
                            val client = Client(
                                name = name,
                                phone = phone,
                                cpf_cnpj = cpf,
                                address = address,
                                email = email
                            )

                            viewModel.createNewOrder(client)
                            navController.navigate(CreateOrderRoute.OrderProductAdd.name)
                        }
                    )
                }
                composable(CreateOrderRoute.OrderProductAdd.name) {
                    ProductForm(
                        onAddProduct = { quantity, description, price ->
                            viewModel.currentOrder.value?.let {
                                val product = Product(
                                    quantity = quantity.toDouble(),
                                    description = description,
                                    price = price.toDouble(),
                                    orderId = it.id
                                )
                                viewModel.addProducts(product)
                            }
                        },
                        onClickNext = {

                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductForm(
    onAddProduct: (quantity: String, description: String, price: String) -> Unit,
    onClickNext: () -> Unit
) {
    var quantity by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
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
        FilledTonalButton(
            onClick = {},
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(stringResource(R.string.add_products_btn))
        }
    }
}


@Composable
fun ClientForm(
    onClickNext: (name: String, phone: String, cpf: String, address: String, email: String) -> Unit,
){
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Text(
            text = stringResource(R.string.client_form_title),
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.titleLarge.fontSize
        )
        HorizontalDivider()
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = {name = it},
            label = { Text(stringResource(R.string.client_form_name)) }
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = phone,
            onValueChange = {phone = it},
            label = { Text(stringResource(R.string.client_form_phone)) }
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = cpf,
            onValueChange = {cpf = it},
            label = { Text(stringResource(R.string.client_form_cpf)) }
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = address,
            onValueChange = {address = it},
            label = { Text(stringResource(R.string.client_form_address)) }
        )
        FilledTonalButton(
            onClick = {
                onClickNext(name, phone, cpf, address, email)
            },
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(stringResource(R.string.create_order_btn))
        }
    }
}



@Preview
@Composable
fun OrderScreenPreview(){
    val viewModelFake  = OrderViewModel(AppDatabase.getInstance(LocalContext.current))
    CreateOrderScreen(viewModelFake)
}