package com.example.simpleorderapplication.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun CreateProductBottomSheet(
    onDismiss: () -> Unit,
    onCreateProduct: (
        description: String,
        quantity: String,
        price: String
    ) -> Unit
) {
    var step  by remember { mutableStateOf(CreateProductStep.Description) }
    var description by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    val focusDescription = remember { FocusRequester() }
    val focusQuantity = remember { FocusRequester() }
    val focusPrice = remember { FocusRequester() }

    val focusManager = LocalFocusManager.current

    fun create(){
        focusManager.clearFocus(force = true)
        onCreateProduct(
            description,
            quantity,
            price,
        )
        onDismiss()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(
                text = "Adicionar Produto",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(16.dp))

            when(step) {
                CreateProductStep.Description -> {
                    // 🔹 Descrição
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descrição", style =  MaterialTheme.typography.titleLarge) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusDescription),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                step = CreateProductStep.Price
                            }
                        ),
                        textStyle = MaterialTheme.typography.displaySmall
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Button(
                            modifier = Modifier.padding(14.dp),
                            enabled = description.isNotEmpty(),
                            onClick = {
                                if(description.isBlank()) return@Button
                                step = CreateProductStep.Price
                            }
                        ){
                            Text("Avançar", style = MaterialTheme.typography.titleMedium)
                        }
                    }



                    LaunchedEffect(Unit) {
                        delay(300)
                        focusDescription.requestFocus()
                    }
                }
                CreateProductStep.Quantity -> {
                    // 🔹 Quantidade
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text("Quantidade", style =  MaterialTheme.typography.titleMedium) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusQuantity),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                               create()
                            }
                        ),
                        textStyle = MaterialTheme.typography.displaySmall
                    )
                    Spacer(Modifier.height(16.dp))
                    Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                        Button(
                            modifier = Modifier.padding(14.dp),
                            onClick = {
                                step = CreateProductStep.Price
                            }
                        ) {
                            Text("Voltar", style = MaterialTheme.typography.titleMedium)
                        }
                        Button(
                            modifier = Modifier.padding(14.dp),
                            onClick = {
                                create()
                            }
                        ) {
                            Text("Finalizar", style = MaterialTheme.typography.titleMedium)
                        }

                    }

                    LaunchedEffect(Unit) {
                        focusQuantity.requestFocus()
                    }
                }

                CreateProductStep.Price -> {
                    // 🔹 Preço
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Preço", style =  MaterialTheme.typography.titleMedium) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusPrice),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                step = CreateProductStep.Quantity

                            }
                        ),
                        textStyle = MaterialTheme.typography.displaySmall
                    )
                    Spacer(Modifier.height(16.dp))
                    Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                        Button(
                            modifier = Modifier.padding(14.dp),
                            onClick = {
                                step = CreateProductStep.Quantity
                            }
                        ) {
                            Text("Voltar", style = MaterialTheme.typography.titleMedium)
                        }
                        Button(
                            modifier = Modifier.padding(14.dp),
                            onClick = {
                               step = CreateProductStep.Quantity
                            }
                        ){
                            Text("Avançar", style = MaterialTheme.typography.titleMedium)
                        }
                    }

                    LaunchedEffect(Unit) {
                        focusPrice.requestFocus()
                    }

                }
            }
        }
    }
}

enum class CreateProductStep {
    Description,
    Quantity,
    Price
}