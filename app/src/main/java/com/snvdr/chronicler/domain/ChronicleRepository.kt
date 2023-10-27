package com.snvdr.chronicler.domain

import com.snvdr.chronicler.utils.DataHandler
import kotlinx.coroutines.flow.Flow

interface ChronicleRepository {

    fun getAllChronicles():Flow<DataHandler<List<ChronicleDto>>>

    suspend fun getSpecificChronicle(id:Long):Flow<DataHandler<ChronicleDto>>

    suspend fun createChronicle(chronicle:ChronicleDto):Flow<DataHandler<Unit>>

    suspend fun deleteAllChronicles():Flow<DataHandler<Unit>>

    suspend fun deleteSpecificChronicle(chronicleDto: ChronicleDto):Flow<DataHandler<Unit>>

}