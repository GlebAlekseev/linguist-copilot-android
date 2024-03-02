@file:OptIn(ExperimentalDecomposeApi::class)

package pro.linguistcopilot.feature.auth

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi

@Composable
fun AuthContent(component: AuthComponent) {
    Text(text = "Auth")
}