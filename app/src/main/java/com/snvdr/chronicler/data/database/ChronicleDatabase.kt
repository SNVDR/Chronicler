package com.snvdr.chronicler.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ChronicleDbEntity::class], version = 1)
@TypeConverters(DataConverter::class)
abstract class ChronicleDatabase:RoomDatabase() {
    abstract fun getChronicleDao():ChronicleDao
}