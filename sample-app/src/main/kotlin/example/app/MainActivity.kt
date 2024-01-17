package example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.ui.Modifier
import nl.jacobras.vexflow.example.ExampleSheet

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text("Desktop sample") })
                    }
                ) { paddingValues ->
                    ExampleSheet(Modifier.padding(paddingValues))
                }
            }
        }
    }
}