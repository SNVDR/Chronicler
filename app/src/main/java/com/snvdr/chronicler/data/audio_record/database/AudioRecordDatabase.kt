package com.snvdr.chronicler.data.audio_record.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AudioRecordDbEntity::class], version = 1)
abstract class AudioRecordDatabase: RoomDatabase() {
    abstract fun audioRecordDAO(): AudioRecordDao
}