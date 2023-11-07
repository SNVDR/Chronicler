package com.snvdr.chronicler.data

import android.util.Log
import com.snvdr.chronicler.data.database.ChronicleDao
import com.snvdr.chronicler.data.database.ChronicleDbEntity
import com.snvdr.chronicler.domain.ChronicleDto
import com.snvdr.chronicler.domain.ChronicleOrder
import com.snvdr.chronicler.domain.ChronicleRepository
import com.snvdr.chronicler.domain.NChronicleOrder
import com.snvdr.chronicler.domain.NOrderType
import com.snvdr.chronicler.domain.OrderType
import com.snvdr.chronicler.domain.SaveChronicleModel
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

    override fun getChroniclesCustomQuery(
        chronicleOrder: ChronicleOrder,
        orderType: OrderType
    ): Flow<DataHandler<List<ChronicleDto>>> = flow{
        Log.d("ROOM_LOG","getChroniclesCustomQuery")
        emit(DataHandler.Loading())
        try {

            val list = when(orderType){
                OrderType.ASC ->{
                    when(chronicleOrder){
                        ChronicleOrder.date -> {
                            dao.getAllChroniclesWithOrderAndAsc(ChronicleOrder.date.name).map {
                                it.toChronicleDto()
                            }
                        }
                        ChronicleOrder.title -> {
                            dao.getAllChroniclesWithOrderAndAsc(ChronicleOrder.title.name).map {
                                it.toChronicleDto()
                            }
                        }
                    }
                }
                OrderType.DESC -> {
                    when(chronicleOrder){
                        ChronicleOrder.date -> {
                            dao.getAllChroniclesWithOrderAndDesc(ChronicleOrder.date.name).map {
                                it.toChronicleDto()
                            }
                        }
                        ChronicleOrder.title -> {
                            dao.getAllChroniclesWithOrderAndDesc(ChronicleOrder.title.name).map {
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

    override fun newGetChroniclesCustomQuery(nChronicleOrder: NChronicleOrder): Flow<DataHandler<List<ChronicleDto>>>  = flow{
        emit(DataHandler.Loading())
        try {

            /*val list = when(orderType){
                OrderType.ASC ->{
                    when(chronicleOrder){
                        ChronicleOrder.date -> {
                            dao.getAllChroniclesWithOrderAndAsc(ChronicleOrder.date.name).map {
                                it.toChronicleDto()
                            }
                        }
                        ChronicleOrder.title -> {
                            dao.getAllChroniclesWithOrderAndAsc(ChronicleOrder.title.name).map {
                                it.toChronicleDto()
                            }
                        }
                    }
                }
                OrderType.DESC -> {
                    when(chronicleOrder){
                        ChronicleOrder.date -> {
                            dao.getAllChroniclesWithOrderAndDesc(ChronicleOrder.date.name).map {
                                it.toChronicleDto()
                            }
                        }
                        ChronicleOrder.title -> {
                            dao.getAllChroniclesWithOrderAndDesc(ChronicleOrder.title.name).map {
                                it.toChronicleDto()
                            }
                        }
                    }
                }
            }*/

            val list = when(nChronicleOrder.nOrderType){
                NOrderType.Ascending -> {
                    when(nChronicleOrder){
                        is NChronicleOrder.Date -> {
                            dao.getAllChroniclesWithOrderAndAsc("date").map {
                                it.toChronicleDto()
                            }
                        }
                        is NChronicleOrder.Title -> {
                            dao.getAllChroniclesWithOrderAndAsc("title").map {
                                it.toChronicleDto()
                            }
                        }
                    }
                }
                NOrderType.Descending -> {
                    when(nChronicleOrder){
                        is NChronicleOrder.Date -> {
                            dao.getAllChroniclesWithOrderAndDesc("date").map {
                                it.toChronicleDto()
                            }
                        }
                        is NChronicleOrder.Title -> {
                            dao.getAllChroniclesWithOrderAndDesc("title").map {
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