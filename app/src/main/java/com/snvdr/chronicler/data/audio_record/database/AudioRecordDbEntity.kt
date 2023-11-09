package com.snvdr.chronicler.data.audio_record.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audio_records")
data class AudioRecordDbEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val filename:String,
    val filePath:String,
    val chronicleId:Long
)