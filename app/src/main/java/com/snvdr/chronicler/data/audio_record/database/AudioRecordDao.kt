package com.snvdr.chronicler.data.audio_record.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AudioRecordDao {
    @Query("SELECT * FROM audio_records")
    fun getAll(): List<AudioRecordDbEntity>
    @Query("DELETE FROM audio_records")
    fun deleteAll()
    @Insert
    fun insert(audioRecordDbEntity: AudioRecordDbEntity)
}