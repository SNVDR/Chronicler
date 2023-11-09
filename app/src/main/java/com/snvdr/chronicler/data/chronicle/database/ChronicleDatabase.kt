package com.snvdr.chronicler.data.chronicle.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.snvdr.chronicler.data.audio_record.database.AudioRecordDbEntity

@Database(entities = [ChronicleDbEntity::class,AudioRecordDbEntity::class], version = 1)
@TypeConverters(DataConverter::class)
abstract class ChronicleDatabase:RoomDatabase() {
    abstract fun getChronicleDao(): ChronicleDao
}