package com.snvdr.chronicler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ramcosta.composedestinations.DestinationsNavHost
import com.snvdr.chronicler.presentation.NavGraphs
import com.snvdr.chronicler.theme.ChroniclerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChroniclerTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}