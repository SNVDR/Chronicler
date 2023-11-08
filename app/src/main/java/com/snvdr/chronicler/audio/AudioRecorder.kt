package com.snvdr.chronicler.audio

import java.io.File

interface AudioRecorder {
    fun start(outputFile:File)
    fun stop()

}