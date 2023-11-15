package com.snvdr.chronicler.domain.chronicle.use_cases

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.snvdr.chronicler.domain.chronicle.ChronicleDto
import com.snvdr.chronicler.domain.chronicle.ChronicleRepository
import com.snvdr.chronicler.utils.DataHandler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import kotlin.random.Random

class GetAllChroniclesTest {
    private lateinit var createChronicleUseCase: CreateChronicleUseCase
    private val mockChronicleRepository = mockk<ChronicleRepository>()
    private var dataList = mutableListOf<ChronicleDto>()

    @Before
    fun setup() {
        createChronicleUseCase = CreateChronicleUseCase(mockChronicleRepository)
        dataList = mutableListOf(
            ChronicleDto(
                id = Random.nextLong(),
                title = "AAA",
                content = "AAA",
                date = LocalDateTime.now().toString()
            ),
            ChronicleDto(
                id = Random.nextLong(),
                title = "BBB",
                content = "BBB",
                date = LocalDateTime.now().toString()
            ),
            ChronicleDto(
                id = Random.nextLong(),
                title = "CCC",
                content = "CCC",
                date = LocalDateTime.now().toString()
            )
        )
    }

    @Test
    fun `successfully get all chronicles`() = runBlocking {
        coEvery { mockChronicleRepository.getAllChronicles() } returns flowOf(
            DataHandler.Success(dataList)
        )
        mockChronicleRepository.getAllChronicles().test {
            val item = awaitItem()
            assertThat(item).isInstanceOf(DataHandler.Success::class.java)
            assertThat((item as DataHandler.Success).data).isEqualTo(dataList)
            awaitComplete()
        }
        coVerify { mockChronicleRepository.getAllChronicles() }
    }

    @Test
    fun `error get all chronicles`() = runBlocking {
        coEvery { mockChronicleRepository.getAllChronicles() } returns flowOf(
            DataHandler.Error(message = "Error of getting data")
        )
        mockChronicleRepository.getAllChronicles().test {
            val item = awaitItem()
            assertThat(item).isInstanceOf(DataHandler.Error::class.java)
            assertThat((item as DataHandler.Error).message).isEqualTo("Error of getting data")
            awaitComplete()
        }
        coVerify { mockChronicleRepository.getAllChronicles() }
    }

    @Test
    fun `emit error with cached data when network error occurs`() = runBlocking{
        coEvery { mockChronicleRepository.getAllChronicles() } returns flowOf(
            DataHandler.Error(
                data = dataList,
                message = "Network error"
            )
        )

        // Act
        mockChronicleRepository.getAllChronicles().test {
            // Assert
            val item = awaitItem()
            assertThat(item).isInstanceOf(DataHandler.Error::class.java)
            assertThat((item as DataHandler.Error).data).isEqualTo(dataList)
            assertThat(item.message).isEqualTo("Network error")
            awaitComplete()
        }
        coVerify { mockChronicleRepository.getAllChronicles() }
    }
}