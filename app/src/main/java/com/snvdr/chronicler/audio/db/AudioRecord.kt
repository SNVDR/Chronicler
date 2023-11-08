package com.snvdr.chronicler.audio.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audio_records")
data class AudioRecord(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val filename:String,
    val filePath:String,
    val date:Long,
    val duration:String
)