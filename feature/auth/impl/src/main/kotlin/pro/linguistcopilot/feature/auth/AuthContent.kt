package pro.linguistcopilot.feature.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AuthContent(component: AuthComponent) {
    Column {
        Text(text = "Auth")
        Button(onClick = component.onCloseAuth) {
            Text(text = "Пропустить авторизацию")
        }
    }
}