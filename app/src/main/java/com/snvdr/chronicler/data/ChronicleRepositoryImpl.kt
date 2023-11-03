package com.snvdr.chronicler.data

import com.snvdr.chronicler.data.database.ChronicleDao
import com.snvdr.chronicler.domain.ChronicleDto
import com.snvdr.chronicler.domain.ChronicleRepository
import com.snvdr.chronicler.domain.toChronicleDbEntity
import com.snvdr.chronicler.domain.toChronicleDto
import com.snvdr.chronicler.utils.DataHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ChronicleRepositoryImpl(private val dao: ChronicleDao) : ChronicleRepository {
    override fun getAllChronicles(): Flow<DataHandler<List<ChronicleDto>>> = flow{
        emit(DataHandler.Loading())
        try {
            val list = dao.getEarliestChronicles().map {
                it.toChronicleDto()
            }
            emit(DataHandler.Success(data = list))
        }catch (e:Exception){
            emit(DataHandler.Error(message = e.localizedMessage?:"Error get list of chronicles from database"))
        }
    }

    override  fun getSpecificChronicle(id: Long): Flow<DataHandler<ChronicleDto>> = flow {
        emit(DataHandler.Loading())
        try {
            val chronicle = dao.getSpecificChronicle(id = id).toChronicleDto()
            emit(DataHandler.Success(data = chronicle))
        }catch (e:Exception){
            emit(DataHandler.Error(message = e.localizedMessage?:"Error get specific chronicle from database"))
        }
    }

    override fun updateChronicle(chronicleDto: ChronicleDto): Flow<DataHandler<Unit>> = flow{
        emit(DataHandler.Loading())
        try {
            dao.updateChronicle(chronicle = chronicleDto.toChronicleDbEntity())
            emit(DataHandler.Success(Unit))
        }catch (e:Exception){
            emit(DataHandler.Error(message = e.localizedMessage?:"Error create chronicle"))
        }
    }

    override  fun createChronicle(chronicle: ChronicleDto): Flow<DataHandler<Unit>> = flow{
        emit(DataHandler.Loading())
        //TODO Simulate delay of creating data
        delay(3000L)
        try {
            dao.insertChronicle(chronicle.toChronicleDbEntity())
            emit(DataHandler.Success(Unit))
        }catch (e:Exception){
            emit(DataHandler.Error(message = e.localizedMessage?:"Error create chronicle"))
        }
    }

    override  fun deleteAllChronicles(): Flow<DataHandler<Unit>> = flow {
        emit(DataHandler.Loading())
        try {
            dao.deleteAllChronicles()
            emit(DataHandler.Success(Unit))
        }catch (e:Exception){
            emit(DataHandler.Error(message = e.localizedMessage?:"Error create chronicle"))
        }
    }

    override  fun deleteSpecificChronicle(chronicleDto: ChronicleDto): Flow<DataHandler<Unit>> = flow{
        emit(DataHandler.Loading())
        try {
            dao.deleteSpecificChronicle(chronicle = chronicleDto.toChronicleDbEntity())
            emit(DataHandler.Success(Unit))
        }catch (e:Exception){
            emit(DataHandler.Error(message = e.localizedMessage?:"Error create chronicle"))
        }
    }

    override fun searchDatabase(query: String): Flow<DataHandler<List<ChronicleDto>>> = flow{
        emit(DataHandler.Loading())
        //TODO Search delay
        delay(1000L)
        try {
            val list =  dao.searchDatabase(query = "%$query%").map {
                it.toChronicleDto()
            }
            emit(DataHandler.Success(data = list))
        }catch (e:Exception){
            emit(DataHandler.Error(message = e.localizedMessage?:"Error get filtered data"))
        }
    }
}