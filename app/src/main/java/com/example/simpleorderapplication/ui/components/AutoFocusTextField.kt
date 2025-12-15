import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import kotlinx.coroutines.delay

@Composable
fun AutoFocusTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null ,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // Pede o foco quando o Composable entra na tela
    LaunchedEffect(Unit) {
        // Delay pequeno ajuda a garantir que o teclado apareça corretamente
        delay(200)
        focusRequester.requestFocus()
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { label?.let { Text(label) }},
        singleLine = true,
        modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Next) },
            onDone = { focusManager.clearFocus() }
        )
    )
}
