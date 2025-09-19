package com.example.simpleorderapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.simpleorderapplication.R
import com.example.simpleorderapplication.data.models.OrderEntity
import com.example.simpleorderapplication.domain.Order
import com.example.simpleorderapplication.ui.components.GoBackIcon
import com.example.simpleorderapplication.ui.components.SimpleOrderAppTopBar
import com.example.simpleorderapplication.ui.viewmodels.OrderViewModel
import kotlinx.coroutines.launch


@Composable
fun CreateOrderScreen(
    navController: NavController,
    viewModel: OrderViewModel,
    orderId: Long
) {
    var order by remember { mutableStateOf<Order?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        order = viewModel.getOrder(orderId)
    }

    Scaffold(
        topBar = {
            SimpleOrderAppTopBar(
                stringResource(R.string.order_screen_title).plus(" $orderId"),
                navegationIcon = { GoBackIcon { navController.popBackStack() } }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            if (order !== null) {
                ClientForm(
                    order!!,
                    onClickNext = { name, phone ->
                        val entity = OrderEntity()
                        entity.clientName = name
                        entity.clientPhone = phone
                        if (orderId > 0) {
                            entity.id = orderId
                            entity.amount = order?.products?.sumOf { it -> it.price * it.quantity } ?: 0
                        }

                        coroutineScope.launch {
                            viewModel.createNewOrder(entity)
                            navController.popBackStack()
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun ClientForm(
    order: Order,
    onClickNext: (name: String, phone: String) -> Unit,
){
    var name by remember { mutableStateOf(order.clientName) }
    var phone by remember { mutableStateOf(order.clientPhone) }

    val phoneFocusRequester = remember { FocusRequester() }

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
            label = { Text(stringResource(R.string.client_form_name)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = {
                    phoneFocusRequester.requestFocus()
                }
            )
        )
        TextField(
            modifier = Modifier.fillMaxWidth().focusRequester(phoneFocusRequester),
            value = phone,
            onValueChange = {phone = it},
            label = { Text(stringResource(R.string.client_form_phone) ) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    onClickNext(name, phone)
                }
            )
        )

        FilledTonalButton(
            onClick = {
                onClickNext(name, phone)
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