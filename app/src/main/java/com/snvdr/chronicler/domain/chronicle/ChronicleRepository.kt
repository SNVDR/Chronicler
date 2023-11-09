package com.snvdr.chronicler.domain.chronicle

import com.snvdr.chronicler.utils.DataHandler
import kotlinx.coroutines.flow.Flow

interface ChronicleRepository {
    fun createChronicle(saveChronicleModel: SaveChronicleModel): Flow<DataHandler<Unit>>
    fun getAllChronicles(): Flow<DataHandler<List<ChronicleDto>>>
    fun getSpecificChronicle(id: Long): Flow<DataHandler<ChronicleDto>>
    fun updateChronicle(chronicleDto: ChronicleDto):Flow<DataHandler<Unit>>
    fun deleteAllChronicles(): Flow<DataHandler<Unit>>
    fun deleteSpecificChronicle(chronicleDto: ChronicleDto): Flow<DataHandler<Unit>>
    fun deleteSpecificChronicleById(id:Long): Flow<DataHandler<Unit>>
    fun searchDatabase(query:String):Flow<DataHandler<List<ChronicleDto>>>
    fun searchDatabaseWithOrder(query:String, chronicleOrder: ChronicleOrder):Flow<DataHandler<List<ChronicleDto>>>
    fun getChroniclesWithOrder(chronicleOrder: ChronicleOrder):Flow<DataHandler<List<ChronicleDto>>>

}