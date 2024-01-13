package example.desktop

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(onCloseRequest = { exitApplication() }) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Desktop sample") })
            }
        ) { paddingValues ->
            Text(
                modifier = Modifier.padding(paddingValues),
                text = "Hello, World!"
            )
        }
    }
}