package com.snvdr.chronicler.data.chronicle

import com.snvdr.chronicler.data.chronicle.database.ChronicleDao
import com.snvdr.chronicler.data.chronicle.database.ChronicleDbEntity
import com.snvdr.chronicler.domain.chronicle.ChronicleDto
import com.snvdr.chronicler.domain.chronicle.ChronicleRepository
import com.snvdr.chronicler.domain.chronicle.ChronicleOrder
import com.snvdr.chronicler.domain.chronicle.OrderType
import com.snvdr.chronicler.domain.chronicle.SaveChronicleModel
import com.snvdr.chronicler.domain.chronicle.toChronicleDbEntity
import com.snvdr.chronicler.domain.chronicle.toChronicleDto
import com.snvdr.chronicler.utils.DataHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChronicleRepositoryImpl @Inject constructor(
    private val dao: ChronicleDao
) : ChronicleRepository {
    override fun getAllChronicles(): Flow<DataHandler<List<ChronicleDto>>> = flow{
        emit(DataHandler.Loading())
        try {
            val list = dao.getLatestChronicles().map {
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

    override  fun createChronicle(saveChronicleModel: SaveChronicleModel): Flow<DataHandler<Unit>> = flow{
        emit(DataHandler.Loading())
        //TODO Simulate delay of creating data
        delay( 1500L)
        try {
            dao.insertChronicle(ChronicleDbEntity(title = saveChronicleModel.title, content = saveChronicleModel.content))
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

    override fun deleteSpecificChronicleById(id: Long): Flow<DataHandler<Unit>> = flow {
        emit(DataHandler.Loading())
        try {
            dao.deleteSpecificChronicleById(id = id)
            emit(DataHandler.Success(Unit))
        }catch (e:Exception){
            emit(DataHandler.Error(message = e.localizedMessage?:"Error delete chronicle"))
        }
    }

    override fun searchDatabase(query: String): Flow<DataHandler<List<ChronicleDto>>> = flow{
        emit(DataHandler.Loading())
        //TODO Search delay
        delay(500L)
        try {
            val list =  dao.searchDatabase(query = "%$query%").map {
                it.toChronicleDto()
            }
            emit(DataHandler.Success(data = list))
        }catch (e:Exception){
            emit(DataHandler.Error(message = e.localizedMessage?:"Error get filtered data"))
        }
    }

    override fun searchDatabaseWithOrder(
        query: String,
        chronicleOrder: ChronicleOrder
    ): Flow<DataHandler<List<ChronicleDto>>> = flow {
        emit(DataHandler.Loading())
        try {
            val list = when(chronicleOrder.orderType){
                OrderType.Ascending -> {
                    when(chronicleOrder){
                        is ChronicleOrder.Date -> {
                            dao.searchDatabaseWithOrderAndAsc(query = query,chronicleOrder = "date").map {
                                it.toChronicleDto()
                            }
                        }
                        is ChronicleOrder.Title -> {
                            dao.searchDatabaseWithOrderAndAsc(query = query,chronicleOrder = "title").map {
                                it.toChronicleDto()
                            }
                        }
                    }
                }
                OrderType.Descending -> {
                    when(chronicleOrder){
                        is ChronicleOrder.Date -> {
                            dao.searchDatabaseWithOrderAndDesc(query = query,chronicleOrder = "date").map {
                                it.toChronicleDto()
                            }
                        }
                        is ChronicleOrder.Title -> {
                            dao.searchDatabaseWithOrderAndDesc(query = query,chronicleOrder = "title").map {
                                it.toChronicleDto()
                            }
                        }
                    }
                }
            }
            emit(DataHandler.Success(data = list))
        }catch (e:Exception){
            emit(DataHandler.Error(message = e.localizedMessage?:"Error get list of chronicles from database"))
        }
    }

    override fun getChroniclesWithOrder(chronicleOrder: ChronicleOrder): Flow<DataHandler<List<ChronicleDto>>>  = flow{
        emit(DataHandler.Loading())
        try {
            val list = when(chronicleOrder.orderType){
                OrderType.Ascending -> {
                    when(chronicleOrder){
                        is ChronicleOrder.Date -> {
                            dao.getChroniclesWithOrderAndAsc("date").map {
                                it.toChronicleDto()
                            }
                        }
                        is ChronicleOrder.Title -> {
                            dao.getChroniclesWithOrderAndAsc("title").map {
                                it.toChronicleDto()
                            }
                        }
                    }
                }
                OrderType.Descending -> {
                    when(chronicleOrder){
                        is ChronicleOrder.Date -> {
                            dao.getChroniclesWithOrderAndDesc("date").map {
                                it.toChronicleDto()
                            }
                        }
                        is ChronicleOrder.Title -> {
                            dao.getChroniclesWithOrderAndDesc("title").map {
                                it.toChronicleDto()
                            }
                        }
                    }
                }
            }


            emit(DataHandler.Success(data = list))
        }catch (e:Exception){
            emit(DataHandler.Error(message = e.localizedMessage?:"Error get list of chronicles from database"))
        }
    }
}