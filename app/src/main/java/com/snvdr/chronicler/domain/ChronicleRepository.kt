package com.snvdr.chronicler.domain

import com.snvdr.chronicler.utils.DataHandler
import kotlinx.coroutines.flow.Flow

interface ChronicleRepository {
    fun createChronicle(saveChronicleModel: SaveChronicleModel): Flow<DataHandler<Unit>>
    fun getAllChronicles(): Flow<DataHandler<List<ChronicleDto>>>
    fun getSpecificChronicle(id: Long): Flow<DataHandler<ChronicleDto>>
    fun updateChronicle(chronicleDto: ChronicleDto):Flow<DataHandler<Unit>>
    fun deleteAllChronicles(): Flow<DataHandler<Unit>>
    fun deleteSpecificChronicle(chronicleDto: ChronicleDto): Flow<DataHandler<Unit>>
    fun searchDatabase(query:String):Flow<DataHandler<List<ChronicleDto>>>
    fun getChroniclesCustomQuery(chronicleOrder: ChronicleOrder, orderType: OrderType):Flow<DataHandler<List<ChronicleDto>>>
    fun newGetChroniclesCustomQuery(nChronicleOrder: NChronicleOrder):Flow<DataHandler<List<ChronicleDto>>>

}