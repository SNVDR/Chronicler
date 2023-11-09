package com.snvdr.chronicler.data.chronicle.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface ChronicleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChronicle(chronicle: ChronicleDbEntity)

    @Query("SELECT * FROM chronicle_table")
    suspend fun getChronicles():List<ChronicleDbEntity>

    @Query("SELECT * FROM chronicle_table WHERE id = :id")
    suspend fun getSpecificChronicle(id:Long): ChronicleDbEntity

    @Update
    suspend fun updateChronicle(chronicle: ChronicleDbEntity)

    @Query("DELETE FROM chronicle_table")
    suspend fun deleteAllChronicles()

    @Delete
    suspend fun deleteSpecificChronicle(chronicle: ChronicleDbEntity)
    @Query("DELETE FROM chronicle_table WHERE id=:id")
    suspend fun deleteSpecificChronicleById(id: Long)

    @Query("SELECT * FROM chronicle_table WHERE title LIKE :query")
    suspend fun searchDatabase(query:String):List<ChronicleDbEntity>
    @Query("SELECT * FROM chronicle_table WHERE title LIKE :query ORDER by CASE WHEN :chronicleOrder = 'date' THEN date END ASC , CASE WHEN :chronicleOrder = 'title' THEN title END ASC")
    suspend fun searchDatabaseWithOrderAndAsc(query:String, chronicleOrder: String):List<ChronicleDbEntity>

    @Query("SELECT * FROM chronicle_table WHERE title LIKE :query ORDER by CASE WHEN :chronicleOrder = 'date' THEN date END DESC , CASE WHEN :chronicleOrder = 'title' THEN title END DESC")
    suspend fun searchDatabaseWithOrderAndDesc(query:String, chronicleOrder: String):List<ChronicleDbEntity>

    @Query("SELECT * FROM chronicle_table ORDER by date DESC")
    suspend fun getLatestChronicles():List<ChronicleDbEntity>

    @Query("SELECT * FROM chronicle_table ORDER BY CASE WHEN :chronicleOrder = 'date' THEN date END ASC, CASE WHEN :chronicleOrder = 'title' THEN title END ASC")
    suspend fun getChroniclesWithOrderAndAsc(chronicleOrder: String): List<ChronicleDbEntity>

    @Query("SELECT * FROM chronicle_table ORDER BY CASE WHEN :chronicleOrder = 'date' THEN date END DESC, CASE WHEN :chronicleOrder = 'title' THEN title END DESC")
    suspend fun getChroniclesWithOrderAndDesc(chronicleOrder: String): List<ChronicleDbEntity>
    @Transaction
    @Query("SELECT * FROM chronicle_table WHERE  id = :id")
    suspend fun getChroniclesWithAudioRecords(id:Long):List<ChronicleWithAudioRecord>


    //DESC - убывание
    //ASC - возростание
}