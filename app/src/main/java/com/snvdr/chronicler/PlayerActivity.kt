package com.snvdr.chronicler

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.snvdr.chronicler.theme.ChroniclerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerActivity : ComponentActivity() {

    private var playbackSpeed :Float = 1.0f
    private lateinit var runnable : Runnable
    private lateinit var handler : Handler
    private lateinit var mediaPlayer : MediaPlayer
    private val delay = 100L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val filePath = intent.getStringExtra("filepath")
        var filename = intent.getStringExtra("filename")

        mediaPlayer = MediaPlayer()
        mediaPlayer.apply {
            setDataSource(filePath)
            prepare()
        }

       setContent {
           ChroniclerTheme {
                PlayAudioScreen(onPlayPause = {
                    playPausePlayer()
                }, onStop = {
                    stopPlayer()
                })
           }
       }
    }

    private fun playPausePlayer(){
        if (!mediaPlayer.isPlaying){
            mediaPlayer.start()
        }else{
            mediaPlayer.pause()
        }
    }
    private fun stopPlayer(){
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}