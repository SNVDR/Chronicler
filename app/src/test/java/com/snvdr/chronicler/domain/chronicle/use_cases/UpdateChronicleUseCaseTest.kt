package com.snvdr.chronicler.domain.chronicle.use_cases

import app.cash.turbine.test
import com.google.common.truth.Truth
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

class UpdateChronicleUseCaseTest{
    private lateinit var updateChronicleUseCase:UpdateChronicleUseCase
    private val mockChronicleRepository = mockk<ChronicleRepository>()
    @Before
    fun setUp() {
        updateChronicleUseCase = UpdateChronicleUseCase(mockChronicleRepository)
    }
    @Test
    fun `success update chronicle`() = runBlocking {
        val model = ChronicleDto(
            id = 1488,
            title = "Update title",
            content = "Update content",
            date = LocalDateTime.now().toString()
        )
        coEvery { mockChronicleRepository.updateChronicle(model) } returns flowOf(
            DataHandler.Success(Unit)
        )
        updateChronicleUseCase(model).test {
            val item = awaitItem()
            Truth.assertThat(item).isInstanceOf(DataHandler.Success::class.java)
            awaitComplete()
        }
        coVerify { mockChronicleRepository.updateChronicle(model) }
    }
    @Test
    fun `error update chronicle`() = runBlocking {
        val model = ChronicleDto(
            id = 1488,
            title = "Update title",
            content = "Update content",
            date = LocalDateTime.now().toString()
        )
        coEvery { mockChronicleRepository.updateChronicle(model) } returns flowOf(
            DataHandler.Error(message = "Can't update model")
        )
        updateChronicleUseCase(model).test {
            val item = awaitItem()
            Truth.assertThat(item).isInstanceOf(DataHandler.Error::class.java)
            awaitComplete()
        }
        coVerify { mockChronicleRepository.updateChronicle(model) }
    }
}