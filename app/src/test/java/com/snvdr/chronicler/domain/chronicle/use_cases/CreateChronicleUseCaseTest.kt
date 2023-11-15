package com.snvdr.chronicler.domain.chronicle.use_cases

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.snvdr.chronicler.domain.chronicle.ChronicleRepository
import com.snvdr.chronicler.domain.chronicle.SaveChronicleModel
import com.snvdr.chronicler.utils.DataHandler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class CreateChronicleUseCaseTest {
    private lateinit var createChronicleUseCase: CreateChronicleUseCase
    private val mockChronicleRepository = mockk<ChronicleRepository>()

    @Before
    fun setup() {
        createChronicleUseCase = CreateChronicleUseCase(mockChronicleRepository)
    }

    @Test
    fun `successfully chronicle creation`() = runBlocking {
        val saveChronicleModel = SaveChronicleModel(
            title = "Title", content = "Content"
        )
        coEvery { mockChronicleRepository.createChronicle(saveChronicleModel = saveChronicleModel) } returns flowOf(
            DataHandler.Success(Unit)
        )
        createChronicleUseCase(saveChronicleModel = saveChronicleModel).test {
            assertThat(awaitItem()).isInstanceOf(DataHandler.Success::class.java)
            awaitComplete()
        }

        coVerify { mockChronicleRepository.createChronicle(saveChronicleModel = saveChronicleModel) }
    }

    @Test
    fun `emit error from use case if title is empty`() = runBlocking {
        val errorSaveChronicleModel = SaveChronicleModel(
            title = "", content = "Content"
        )
        coEvery { mockChronicleRepository.createChronicle(saveChronicleModel = errorSaveChronicleModel) } returns flowOf(
            DataHandler.Error(message = "Title can't be empty")
        )
        createChronicleUseCase(saveChronicleModel = errorSaveChronicleModel).test {
            val item = awaitItem()
            assertThat(item).isInstanceOf(DataHandler.Error::class.java)
            assertThat(item.message).isEqualTo("Title can't be empty")
            awaitComplete()
        }
        coVerify(exactly = 0) { mockChronicleRepository.createChronicle(saveChronicleModel = errorSaveChronicleModel) }
    }

    @Test
    fun `emit create chronicle failure from repository`() = runBlocking {
        val saveChronicleModel = SaveChronicleModel(
            title = "Title", content = "Content"
        )
        coEvery { mockChronicleRepository.createChronicle(saveChronicleModel = saveChronicleModel) } returns flowOf(
            DataHandler.Error(message = "Unknown error")
        )
        createChronicleUseCase(saveChronicleModel = saveChronicleModel).test {
            assertThat(awaitItem()).isInstanceOf(DataHandler.Error::class.java)
            awaitComplete()
        }

        coVerify { mockChronicleRepository.createChronicle(saveChronicleModel = saveChronicleModel) }
    }
}