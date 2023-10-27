package com.snvdr.chronicler.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chronicle_table")
data class ChronicleDbEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    val title:String,
    val content:String
)