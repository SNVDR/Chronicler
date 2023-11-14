package com.snvdr.chronicler.domain.chronicle.use_cases

import app.cash.turbine.test
import com.google.common.truth.Truth.*
import com.snvdr.chronicler.domain.chronicle.ChronicleDto
import com.snvdr.chronicler.domain.chronicle.ChronicleOrder
import com.snvdr.chronicler.domain.chronicle.ChronicleRepository
import com.snvdr.chronicler.domain.chronicle.OrderType
import com.snvdr.chronicler.domain.chronicle.SaveChronicleModel
import com.snvdr.chronicler.utils.DataHandler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.LocalDateTime
import kotlin.random.Random

class GetAllChroniclesUseCaseTest {

    private val fakeRepository = mockk<ChronicleRepository>()

    @Test
    fun `should return empty list`() = runBlocking {
        val useCase = GetAllChroniclesWithOrder(chronicleRepository = fakeRepository)
        val orderSetup = ChronicleOrder.Title(OrderType.Ascending)
        coEvery { fakeRepository.getChroniclesWithOrder(chronicleOrder = orderSetup) } returns flow{
            emit(DataHandler.Loading())
            emit(DataHandler.Success(emptyList()))
        }

        useCase(chronicleOrder = orderSetup).test {
            val loadingItem = awaitItem()
            assertThat(loadingItem).isInstanceOf(DataHandler.Loading::class.java)

            val successItem = awaitItem()
            assertThat(successItem).isInstanceOf(DataHandler.Success::class.java)

            assertThat(successItem.data).isEmpty()

            awaitComplete()
        }

        coVerify(){fakeRepository.getChroniclesWithOrder(chronicleOrder = orderSetup)}
    }

    @Test
    fun `should return list with data`() = runBlocking {
        val useCase = GetAllChroniclesWithOrder(chronicleRepository = fakeRepository)
        val orderSetup = ChronicleOrder.Title(OrderType.Ascending)
        val fakeDataForList = listOf(
            ChronicleDto(id = Random.nextLong(), title = "Title", content = "Content", date = LocalDateTime.now().toString()),
            ChronicleDto(id = Random.nextLong(), title = "Title", content = "Content", date = LocalDateTime.now().toString()),
            ChronicleDto(id = Random.nextLong(), title = "Title", content = "Content", date = LocalDateTime.now().toString())
        )
        coEvery { fakeRepository.getChroniclesWithOrder(chronicleOrder = orderSetup) } returns flow{
            emit(DataHandler.Loading())
            emit(DataHandler.Success(fakeDataForList))
        }

        useCase(chronicleOrder = orderSetup).test {
            val loadingItem = awaitItem()
            assertThat(loadingItem).isInstanceOf(DataHandler.Loading::class.java)

            val successItem = awaitItem()
            assertThat(successItem).isInstanceOf(DataHandler.Success::class.java)

            assertThat(successItem.data).isNotEmpty()

            awaitComplete()
        }

        coVerify(){fakeRepository.getChroniclesWithOrder(chronicleOrder = orderSetup)}
    }

}