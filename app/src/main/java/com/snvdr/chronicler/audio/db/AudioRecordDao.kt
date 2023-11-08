package com.snvdr.chronicler.audio.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AudioRecordDao {
    @Query("SELECT * FROM audio_records")
    fun getAll(): List<AudioRecord>

    @Query("SELECT * FROM audio_records WHERE filename LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): List<AudioRecord>

    @Query("DELETE FROM audio_records")
    fun deleteAll()

    @Insert
    fun insert(vararg audioRecord: AudioRecord)
}