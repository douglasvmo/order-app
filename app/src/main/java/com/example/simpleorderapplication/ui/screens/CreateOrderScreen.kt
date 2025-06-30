package com.example.simpleorderapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.example.simpleorderapplication.data.models.OrderEntity
import com.example.simpleorderapplication.ui.components.SimpleOrderAppTopBar
import com.example.simpleorderapplication.ui.viewmodels.OrderViewModel
import kotlinx.coroutines.launch


@Composable
fun CreateOrderScreen(
    navController: NavController,
    viewModel: OrderViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            SimpleOrderAppTopBar(
                stringResource(R.string.order_screen_title).plus(" x"),
                onGoBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            ClientForm(
                onClickNext = { name, phone, cpf, address, email ->
                    val order = OrderEntity().apply {
                        clientName = name
                    }

                    coroutineScope.launch {
                        viewModel.createNewOrder(order)
                        navController.popBackStack()
                    }
                }
            )
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

}