package com.snvdr.chronicler.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ChronicleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChronicle(chronicle:ChronicleDbEntity)

    @Query("SELECT * FROM chronicle_table")
    suspend fun getAllChronicles():List<ChronicleDbEntity>

    @Query("SELECT * FROM chronicle_table WHERE id = :id")
    suspend fun getSpecificChronicle(id:Long):ChronicleDbEntity

    @Update
    suspend fun updateChronicle(chronicle: ChronicleDbEntity)

    @Query("DELETE FROM chronicle_table")
    suspend fun deleteAllChronicles()

    @Delete
    suspend fun deleteSpecificChronicle(chronicle: ChronicleDbEntity)

    @Query("SELECT * FROM chronicle_table WHERE title LIKE :query")
    suspend fun searchDatabase(query:String):List<ChronicleDbEntity>

    @Query("SELECT * FROM chronicle_table ORDER by date DESC")
    suspend fun getLatestChronicles():List<ChronicleDbEntity>

    @Query("SELECT * FROM chronicle_table ORDER by date ASC")
    suspend fun getEarliestChronicles():List<ChronicleDbEntity>

}