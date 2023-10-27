package com.snvdr.chronicler.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.snvdr.chronicler.presentation.main_screen.ChronicleMainScreen
import com.snvdr.chronicler.presentation.theme.ChroniclerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChroniclerTheme {
                ChronicleMainScreen()
            }
        }
    }
}