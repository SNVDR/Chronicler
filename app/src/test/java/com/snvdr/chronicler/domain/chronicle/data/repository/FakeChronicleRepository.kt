package com.snvdr.chronicler.domain.chronicle.data.repository

import com.snvdr.chronicler.domain.chronicle.ChronicleDto
import com.snvdr.chronicler.domain.chronicle.ChronicleOrder
import com.snvdr.chronicler.domain.chronicle.ChronicleRepository
import com.snvdr.chronicler.domain.chronicle.OrderType
import com.snvdr.chronicler.domain.chronicle.SaveChronicleModel
import com.snvdr.chronicler.utils.DataHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import kotlin.random.Random

class FakeChronicleRepository : ChronicleRepository {
     val chronicles = mutableListOf<ChronicleDto>()
    override fun createChronicle(saveChronicleModel: SaveChronicleModel): Flow<DataHandler<Unit>> =
        flow {
            try {
                emit(DataHandler.Loading())
                val fakeModel = SaveChronicleModel(title = "Fake title", "Fake content")
                val fakeChronicle = ChronicleDto(
                    Random.nextLong(), fakeModel.title, fakeModel.content,
                    date = LocalDateTime.now().toString()
                )
                chronicles.add(fakeChronicle)
                emit(DataHandler.Success(Unit))
            } catch (e: Exception) {
                emit(DataHandler.Error(message = "Can't create chronicle"))
            }
        }

    override fun getAllChronicles(): Flow<DataHandler<List<ChronicleDto>>> = flow {
        try {
            emit(DataHandler.Loading())
            emit(DataHandler.Success(chronicles))
        } catch (e: Exception) {
            emit(DataHandler.Error(message = "Can't get chronicles"))
        }
    }

    override fun getSpecificChronicle(id: Long): Flow<DataHandler<ChronicleDto>> = flow {
        try {
            emit(DataHandler.Loading())
            val x = chronicles.find { it.id == id }
            if (x == null){
                emit(DataHandler.Error(message = "Can't find chronicle with id:$id"))
            }else{
                emit(DataHandler.Success(x))
            }
        } catch (e: Exception) {
            emit(DataHandler.Error(message = "Can't find chronicle by id"))
        }
    }

    override fun updateChronicle(chronicleDto: ChronicleDto): Flow<DataHandler<Unit>> = flow{
        try {
            emit(DataHandler.Loading())
            val x = chronicles.find {
                it.id == chronicleDto.id
            }
            if (x == null){
                emit(DataHandler.Error(message = "Can't find and update chronicle with id:${chronicleDto.id}"))
            }else{
                emit(DataHandler.Success(Unit))
            }
        }catch (e:Exception){
            emit(DataHandler.Error(message = "Can't update chronicle"))
        }
    }

    override fun deleteAllChronicles(): Flow<DataHandler<Unit>> {
        chronicles.clear()
        return flow { emit(DataHandler.Success(Unit)) }
    }

    override fun deleteSpecificChronicle(chronicleDto: ChronicleDto): Flow<DataHandler<Unit>> = flow{
      try {
          emit(DataHandler.Loading())
          val x = chronicles.removeIf{
              it.id == chronicleDto.id
          }
          if (!x){
              emit(DataHandler.Error(message = "Can't find and delete chronicle with id:${chronicleDto.id}"))
          }else{
              emit(DataHandler.Success(Unit))
          }
      }catch (e:Exception){
          emit(DataHandler.Error(message = "Cant delete chronicle"))
      }
    }

    override fun deleteSpecificChronicleById(id: Long): Flow<DataHandler<Unit>> = flow {
        try {
            emit(DataHandler.Loading())
            val x = chronicles.removeIf{
                it.id == id
            }
            if (!x){
                emit(DataHandler.Error(message = "Can't find and delete chronicle with id:$id"))
            }else{
                emit(DataHandler.Success(Unit))
            }
        }catch (e:Exception){
            emit(DataHandler.Error(message = "Cant delete chronicle"))
        }
    }

    override fun searchDatabase(query: String): Flow<DataHandler<List<ChronicleDto>>> = flow {
        try {
            emit(DataHandler.Loading())
            val x = chronicles.filter {
                it.title.contains("%$query%") || it.content.contains("%$query%")
            }
            if (x.isEmpty()){
                emit(DataHandler.Error(message = "Can't find chronicles which contains:$query"))
            }else{
                emit(DataHandler.Success(x))
            }
        }catch (e:Exception){
            emit(DataHandler.Error(message = "Cant delete chronicle"))
        }
    }

    override fun searchDatabaseWithOrder(
        query: String,
        chronicleOrder: ChronicleOrder
    ): Flow<DataHandler<List<ChronicleDto>>> = flow {
        try {
            emit(DataHandler.Loading())
            val z = chronicles.filter {
                it.title.contains("%$query%") || it.content.contains("%$query%")
            }
            if (z.isEmpty()){
                emit(DataHandler.Error(message = "Can't find chronicles which contains:$query"))
            }else{
                when(chronicleOrder){
                    is ChronicleOrder.Date ->{
                        when(chronicleOrder.orderType){
                            OrderType.Ascending -> {
                                val x = z.sortedBy {
                                    it.date
                                }
                                emit(DataHandler.Success(x))
                            }
                            OrderType.Descending -> {
                                val x = z.sortedByDescending {
                                    it.date
                                }
                                emit(DataHandler.Success(x))
                            }
                        }
                    }
                    is ChronicleOrder.Title -> {
                        when(chronicleOrder.orderType){
                            OrderType.Ascending -> {
                                val x = z.sortedBy {
                                    it.title
                                }
                                emit(DataHandler.Success(x))
                            }
                            OrderType.Descending -> {
                                val x = z.sortedByDescending {
                                    it.title
                                }
                                emit(DataHandler.Success(x))
                            }
                        }
                    }
                }
            }
        }catch (e:Exception){
            emit(DataHandler.Error(message = "Cant find chronicles by query and sort it"))
        }
    }

    override fun getChroniclesWithOrder(chronicleOrder: ChronicleOrder): Flow<DataHandler<List<ChronicleDto>>>  = flow{
        try {
            emit(DataHandler.Loading())
            val list = chronicles
            when(chronicleOrder){
                is ChronicleOrder.Date ->{
                    when(chronicleOrder.orderType){
                        OrderType.Ascending -> {
                            val x = list.sortedBy {
                                it.date
                            }
                            emit(DataHandler.Success(x))
                        }
                        OrderType.Descending -> {
                            val x = list.sortedByDescending {
                                it.date
                            }
                            emit(DataHandler.Success(x))
                        }
                    }
                }
                is ChronicleOrder.Title -> {
                    when(chronicleOrder.orderType){
                        OrderType.Ascending -> {
                            val x = list.sortedBy {
                                it.title
                            }
                            emit(DataHandler.Success(x))
                        }
                        OrderType.Descending -> {
                            val x = list.sortedByDescending {
                                it.title
                            }
                            emit(DataHandler.Success(x))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            emit(DataHandler.Error(message = "Cant find chronicles and sort it"))
        }
    }
}