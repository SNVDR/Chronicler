package com.snvdr.chronicler.domain.audio_recorder

import java.io.File

interface AudioRecorder {
    fun start(outputFile:File)
    fun stop()

}