import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrderBottomSheet(
    onDismiss: () -> Unit,
    onCreateOrder: (clientName: String, clientPhone: String, clientCPF: String) -> Unit
) {
    var step by remember { mutableStateOf(CreateOrderStep.ClientName) }
    var clientName by remember { mutableStateOf("") }
    var clientPhone by remember { mutableStateOf("") }
    var clientCPF by remember { mutableStateOf("") }

    val focusName = remember { FocusRequester() }
    val focusPhone = remember { FocusRequester() }
    val focusCPF = remember { FocusRequester() }

    val focusManager = LocalFocusManager.current


    ModalBottomSheet(
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            when (step) {
                CreateOrderStep.ClientName -> {
                    Text("Nome do Cliente", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = clientName,
                        onValueChange = { clientName = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusName),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (clientName.isNotEmpty()) {
                                    step = CreateOrderStep.ClientPhone
                                }
                            }
                        ),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words
                        ),
                        textStyle = MaterialTheme.typography.displaySmall
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Button(
                            modifier = Modifier.padding(14.dp),
                            enabled = clientName.isNotEmpty(),
                            onClick = {
                                step = CreateOrderStep.ClientPhone
                            }
                        ) {
                            Text("Avançar", style = MaterialTheme.typography.titleLarge)
                        }
                    }


                    LaunchedEffect(Unit) {
                        delay(300)
                        focusName.requestFocus()
                    }
                }
                CreateOrderStep.ClientPhone -> {
                    Text("Telefone do Cliente", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = clientPhone,
                        onValueChange = { clientPhone = it },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                step = CreateOrderStep.ClientCPF
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusPhone),
                        textStyle = MaterialTheme.typography.displaySmall
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        TextButton(onClick = { step = CreateOrderStep.ClientName }, Modifier.padding(14.dp)) {
                            Text("Voltar", style = MaterialTheme.typography.titleMedium)
                        }
                        Button(
                            onClick = {
                                step = CreateOrderStep.ClientCPF
                            },
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Text("Avançar", style = MaterialTheme.typography.titleLarge)
                        }
                    }

                    LaunchedEffect(Unit) {
                        focusPhone.requestFocus()
                    }
                }
                CreateOrderStep.ClientCPF -> {
                    Text("CPF do Cliente", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = clientCPF,
                        onValueChange = { clientCPF = it },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        keyboardActions= KeyboardActions(
                            onDone = {
                                focusManager.clearFocus(force = true)
                                onCreateOrder(clientName, clientPhone, clientCPF)
                                onDismiss()
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusCPF),
                        textStyle = MaterialTheme.typography.displaySmall
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        TextButton(onClick = { step = CreateOrderStep.ClientPhone }, Modifier.padding(14.dp)) {
                            Text("Voltar", style = MaterialTheme.typography.titleMedium)
                        }
                        Button(onClick = {
                            focusManager.clearFocus(force = true)
                            onCreateOrder(clientName, clientPhone, clientCPF)
                            onDismiss()
                        }, modifier = Modifier.padding(14.dp)) {
                            Text("Finalizar", style =  MaterialTheme.typography.titleLarge)
                        }
                    }
                    LaunchedEffect(Unit) {
                        focusCPF.requestFocus()
                    }
                }
                else -> {}
            }
        }
    }
}

enum class CreateOrderStep {
    ClientName,
    ClientPhone,
    ClientCPF,
    Confirm
}