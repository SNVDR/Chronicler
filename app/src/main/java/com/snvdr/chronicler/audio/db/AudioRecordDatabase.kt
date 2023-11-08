package com.snvdr.chronicler.audio.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.snvdr.chronicler.data.database.ChronicleDao
import com.snvdr.chronicler.data.database.ChronicleDbEntity

@Database(entities = [AudioRecord::class], version = 1)
abstract class AudioRecordDatabase: RoomDatabase() {
    abstract fun audioRecordDAO(): AudioRecordDao
}