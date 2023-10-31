package com.snvdr.chronicler.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "chronicle_table")
@Parcelize
data class ChronicleDbEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    val title:String,
    val content:String,
    val date: LocalDateTime = LocalDateTime.now()
): Parcelable{
    val createdDateFormatted : String
        get() = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy:HH:mm:ss"))
}