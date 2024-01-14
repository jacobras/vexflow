package example.desktop

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import nl.jacobras.vexflowcompose.example.ExampleSheet

fun main() = application {
    Window(onCloseRequest = { exitApplication() }) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("VexFlow Compose sample") })
            }
        ) { paddingValues ->
            ExampleSheet(Modifier.padding(paddingValues))
        }
    }
}