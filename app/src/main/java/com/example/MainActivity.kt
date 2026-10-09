package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.navigation.MainAppNavigation
import com.example.ui.theme.BamBamJankiTheme
import com.example.ui.viewmodel.HotelViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BamBamJankiTheme {
                val viewModel: HotelViewModel = viewModel()
                MainAppNavigation(viewModel = viewModel)
            }
        }
    }
}
