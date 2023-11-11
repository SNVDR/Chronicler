package com.snvdr.chronicler.domain.audio_recorder

import java.io.File

interface AudioPlayer {
    fun playFile(file:File)
    fun stop()
}