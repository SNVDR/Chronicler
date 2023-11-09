package com.snvdr.chronicler

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import com.ramcosta.composedestinations.DestinationsNavHost
import com.snvdr.chronicler.presentation.NavGraphs
import com.snvdr.chronicler.theme.ChroniclerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            0
        )
        setContent {
            ChroniclerTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}