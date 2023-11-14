package com.snvdr.chronicler.domain.chronicle.use_cases

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.snvdr.chronicler.domain.chronicle.ChronicleRepository
import com.snvdr.chronicler.domain.chronicle.SaveChronicleModel
import com.snvdr.chronicler.utils.DataHandler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CreateChronicleUseCaseTest {

    private val fakeRepository = mockk<ChronicleRepository>()

    @Test
    fun `pass empty title should emit error and doesn't call repository`() = runBlocking{
        val errorSaveModel = SaveChronicleModel(title = "", content = "")

        val useCase = CreateChronicleUseCase(chronicleRepository = fakeRepository)

        useCase(saveChronicleModel = errorSaveModel).test {
            val errorItem = awaitItem()
            assertThat(errorItem).isInstanceOf(DataHandler.Error::class.java)
            assertThat(errorItem.message).isEqualTo("Title can't be empty")

            awaitComplete()
        }

        coVerify (exactly = 0){fakeRepository.createChronicle(saveChronicleModel = errorSaveModel)}
    }

    @Test
    fun `pass correct save model should call repository and return success`() = runBlocking(){
        val correctSaveModel = SaveChronicleModel(title = "SS", content = "SS")

        val useCase = CreateChronicleUseCase(chronicleRepository = fakeRepository)

        coEvery { fakeRepository.createChronicle(saveChronicleModel = correctSaveModel) } returns flow {
            emit(DataHandler.Loading())
            emit(DataHandler.Success(Unit))
        }

        useCase(saveChronicleModel = correctSaveModel).test {
            val loadingItem = awaitItem()
            assertThat(loadingItem).isInstanceOf(DataHandler.Loading::class.java)

            val successItem = awaitItem()
            assertThat(successItem).isInstanceOf(DataHandler.Success::class.java)

            awaitComplete()
        }

        coVerify(){fakeRepository.createChronicle(saveChronicleModel = correctSaveModel)}
    }

}