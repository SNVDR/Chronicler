package com.snvdr.chronicler.data.audio_record

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import com.snvdr.chronicler.domain.audio.AudioPlayer
import java.io.File

class AndroidAudioPlayer(private val context: Context): AudioPlayer {
    private var player: MediaPlayer? = null
    override fun playFile(file: File) {
        MediaPlayer.create(context, file.toUri()).apply {
            player = this
            start()
        }
    }

    override fun stop() {
        player?.start()
        player?.release()
        player = null
    }

}