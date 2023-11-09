package com.snvdr.chronicler.domain.audio

import java.io.File

interface AudioPlayer {
    fun playFile(file:File)
    fun stop()
}