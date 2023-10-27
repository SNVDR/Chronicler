package com.snvdr.chronicler.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ChronicleDbEntity::class], version = 1)
abstract class ChronicleDatabase:RoomDatabase() {
    abstract fun getChronicleDao():ChronicleDao
}