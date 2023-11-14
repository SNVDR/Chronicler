package com.snvdr.chronicler.domain.chronicle.old_maybe_delete.data.repository

import com.snvdr.chronicler.domain.chronicle.ChronicleDto
import com.snvdr.chronicler.domain.chronicle.ChronicleOrder
import com.snvdr.chronicler.domain.chronicle.ChronicleRepository
import com.snvdr.chronicler.domain.chronicle.OrderType
import com.snvdr.chronicler.domain.chronicle.SaveChronicleModel
import com.snvdr.chronicler.utils.DataHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import kotlin.random.Random

class FakeChronicleRepository : ChronicleRepository {

    private val chronicleItems = mutableListOf<ChronicleDto>()
    override fun createChronicle(saveChronicleModel: SaveChronicleModel): Flow<DataHandler<Unit>> =
        flow {
            emit(DataHandler.Loading())
            val newChronicle = ChronicleDto(
                id = Random.nextLong(),
                title = saveChronicleModel.title,
                content = saveChronicleModel.content,
                date = LocalDateTime.now().toString()
            )
            chronicleItems.add(newChronicle)
            emit(DataHandler.Success(Unit))
        }.catch { e ->
            emit(DataHandler.Error(message = e.message ?: "Can't create chronicle"))
        }

    override fun getAllChronicles(): Flow<DataHandler<List<ChronicleDto>>> = flow {
        try {
            emit(DataHandler.Loading())
            emit(DataHandler.Success(chronicleItems))
        } catch (e: Exception) {
            emit(DataHandler.Error(message = e.message ?: "Can't get chronicles"))
        }
    }

    override fun getSpecificChronicle(id: Long): Flow<DataHandler<ChronicleDto>> = flow {
        try {
            emit(DataHandler.Loading())
            val specificChronicle = chronicleItems.find { it.id == id }
            if (specificChronicle == null) {
                emit(DataHandler.Error(message = "Can't find chronicle with id:$id"))
            } else {
                emit(DataHandler.Success(specificChronicle))
            }
        } catch (e: Exception) {
            emit(DataHandler.Error(message = e.message ?: "Can't get chronicle"))
        }
    }

    override fun updateChronicle(chronicleDto: ChronicleDto): Flow<DataHandler<Unit>> = flow {
        try {
            emit(DataHandler.Loading())
            val chronicle = chronicleItems.find { it.id == chronicleDto.id }
            if (chronicle == null) {
                emit(DataHandler.Error(message = "You try to update non-existent chronicle"))
            } else {
                chronicleItems.add(chronicleDto)
                emit(DataHandler.Success(Unit))
            }
        } catch (e: Exception) {
            emit(DataHandler.Error(message = e.message ?: "Can't update chronicle"))
        }
    }

    override fun deleteAllChronicles(): Flow<DataHandler<Unit>> = flow {
        try {
            emit(DataHandler.Loading())
            chronicleItems.clear()
            emit(DataHandler.Success(Unit))
        } catch (e: Exception) {
            emit(DataHandler.Error(message = e.message ?: "Can't delete all chronicles"))

        }
    }

    override fun deleteSpecificChronicle(chronicleDto: ChronicleDto): Flow<DataHandler<Unit>> =
        flow {
            try {
                emit(DataHandler.Loading())
                val chronicleToDelete = chronicleItems.find { it.id == chronicleDto.id }
                if (chronicleToDelete == null) {
                    emit(DataHandler.Error(message = "Can't delete chronicle with id:${chronicleDto.id}, because it doesn't exist"))
                } else {
                    chronicleItems.remove(chronicleToDelete)
                    emit(DataHandler.Success(Unit))
                }
            } catch (e: Exception) {
                emit(DataHandler.Error(message = e.message ?: "Can't delete specific chronicle"))
            }
        }

    override fun deleteSpecificChronicleById(id: Long): Flow<DataHandler<Unit>> = flow {
        try {
            emit(DataHandler.Loading())
            val chronicleToDelete = chronicleItems.find { it.id == id }
            if (chronicleToDelete == null) {
                emit(DataHandler.Error(message = "Can't delete chronicle with id:${id}, because it doesn't exist"))
            } else {
                chronicleItems.remove(chronicleToDelete)
                emit(DataHandler.Success(Unit))
            }
        } catch (e: Exception) {
            emit(DataHandler.Error(message = e.message ?: "Can't delete specific chronicle"))
        }
    }

    override fun searchDatabase(query: String): Flow<DataHandler<List<ChronicleDto>>> = flow {
        try {
            emit(DataHandler.Loading())
            val itemsByQuery = chronicleItems.filter {
                it.title.contains("%$query%") || it.content.contains("%$query%")
            }
            if (itemsByQuery.isEmpty()) {
                emit(DataHandler.Error(message = "Can't find chronicles which contains:$query"))
            } else {
                emit(DataHandler.Success(itemsByQuery))
            }
        } catch (e: Exception) {
            emit(DataHandler.Error(message = "Cant delete chronicle"))
        }
    }

    override fun searchDatabaseWithOrder(
        query: String,
        chronicleOrder: ChronicleOrder
    ): Flow<DataHandler<List<ChronicleDto>>> = flow {
        try {
            emit(DataHandler.Loading())
            val itemsByQuery = chronicleItems.filter {
                it.title.contains("%$query%") || it.content.contains("%$query%")
            }
            if (itemsByQuery.isEmpty()) {
                emit(DataHandler.Error(message = "Can't find chronicles which contains:$query"))
            } else {
                when (chronicleOrder) {
                    is ChronicleOrder.Date -> {
                        when (chronicleOrder.orderType) {
                            OrderType.Ascending -> {
                                val filteredList = itemsByQuery.sortedBy {
                                    it.date
                                }
                                emit(DataHandler.Success(filteredList))
                            }

                            OrderType.Descending -> {
                                val filteredList = itemsByQuery.sortedByDescending {
                                    it.date
                                }
                                emit(DataHandler.Success(filteredList))
                            }
                        }
                    }

                    is ChronicleOrder.Title -> {
                        when (chronicleOrder.orderType) {
                            OrderType.Ascending -> {
                                val filteredList = itemsByQuery.sortedBy {
                                    it.title
                                }
                                emit(DataHandler.Success(filteredList))
                            }

                            OrderType.Descending -> {
                                val filteredList = itemsByQuery.sortedByDescending {
                                    it.title
                                }
                                emit(DataHandler.Success(filteredList))
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            emit(DataHandler.Error(message = "Cant find chronicles by query and sort it"))
        }
    }

    override fun getChroniclesWithOrder(chronicleOrder: ChronicleOrder): Flow<DataHandler<List<ChronicleDto>>> =
        flow {
            try {
                emit(DataHandler.Loading())
                when (chronicleOrder) {
                    is ChronicleOrder.Date -> {
                        when (chronicleOrder.orderType) {
                            OrderType.Ascending -> {
                                val sortedList = chronicleItems.sortedBy {
                                    it.date
                                }
                                emit(DataHandler.Success(sortedList))
                            }

                            OrderType.Descending -> {
                                val sortedList = chronicleItems.sortedByDescending {
                                    it.date
                                }
                                emit(DataHandler.Success(sortedList))
                            }
                        }
                    }

                    is ChronicleOrder.Title -> {
                        when (chronicleOrder.orderType) {
                            OrderType.Ascending -> {
                                val sortedList = chronicleItems.sortedBy {
                                    it.title
                                }
                                emit(DataHandler.Success(sortedList))
                            }

                            OrderType.Descending -> {
                                val x = chronicleItems.sortedByDescending {
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