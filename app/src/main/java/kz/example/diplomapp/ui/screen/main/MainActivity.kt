package kz.example.diplomapp.ui.screen.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import kz.example.diplomapp.ui.screens.MainScreen
import kz.example.diplomapp.ui.theme.DiplomAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiplomAppTheme() {
                MainScreen()
            }
        }
    }
}