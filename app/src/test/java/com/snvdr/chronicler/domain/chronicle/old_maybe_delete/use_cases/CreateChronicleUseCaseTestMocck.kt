package com.snvdr.chronicler.domain.chronicle.old_maybe_delete.use_cases

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.snvdr.chronicler.domain.chronicle.ChronicleRepository
import com.snvdr.chronicler.domain.chronicle.SaveChronicleModel
import com.snvdr.chronicler.domain.chronicle.use_cases.CreateChronicleUseCase
import com.snvdr.chronicler.utils.DataHandler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CreateChronicleUseCaseTestMocck {
    // Mock your repository
    private val chronicleRepository: ChronicleRepository = mockk()

    // Create an instance of your use case
    private val useCase = CreateChronicleUseCase(chronicleRepository)

  /*  @Test
    fun `invoke with valid data should call repository`() = runBlocking {
        // Arrange
        val saveChronicleModel = SaveChronicleModel(title = "", content = "Content")

        // Mock repository behavior
        coEvery { chronicleRepository.createChronicle(saveChronicleModel) } returns flowOf(
            DataHandler.Success(Unit)
        )

        // Act
        val result = useCase(saveChronicleModel)

        // Assert
        result.collect { dataHandler ->
            // Use Truth for assertions
            assertThat(dataHandler).isInstanceOf(DataHandler.Success::class.java)
        }

        // Verify that the repository function was called with the correct parameters
        coVerify { chronicleRepository.createChronicle(saveChronicleModel) }
    }*/

    @Test
    fun `invoke with invalid data should return error`() = runBlocking {
        // Arrange
        val saveChronicleModel = SaveChronicleModel(title = "", content = "")

        coEvery { chronicleRepository.createChronicle(any()) } returns flowOf(
            DataHandler.Error(
                message = "Title can't be empty"
            )
        )

        // Act
        val result = useCase(saveChronicleModel)

        // Assert
        result.collect { dataHandler ->
            // Use Truth for assertions
            assertThat(dataHandler).isInstanceOf(DataHandler.Error::class.java)
            assertThat((dataHandler as DataHandler.Error<Unit>).message).isEqualTo("Title can't be empty")
        }

        // Verify that the repository function was not called
        coVerify(exactly = 0) { chronicleRepository.createChronicle(saveChronicleModel) }
    }

    @Test
    fun `get all chronicles`() = runBlocking {
        coEvery { chronicleRepository.getAllChronicles() } returns flowOf(
            DataHandler.Success(
                emptyList()
            )
        )

        val result = chronicleRepository.getAllChronicles()
        result.collect { dataHandler ->
            assertThat(dataHandler).isInstanceOf(DataHandler.Success::class.java)
        }
        coVerify { chronicleRepository.getAllChronicles() }
    }

    @Test
    fun `should emit two values`() = runBlocking {
        coEvery { chronicleRepository.getAllChronicles() } returns flow {
            emit(DataHandler.Loading())
            emit(DataHandler.Success(emptyList()))
        }
        chronicleRepository.getAllChronicles().test {
            val firstItem = awaitItem()
            assertThat(firstItem).isInstanceOf(DataHandler.Loading::class.java)

            val secondItem = awaitItem()
            assertThat(secondItem).isInstanceOf(DataHandler.Success::class.java)

            awaitComplete()
        }
    }

    @Test
    fun `getAllChronicles throw error`() = runBlocking {
        coEvery { chronicleRepository.getAllChronicles() } returns flow {
            emit(DataHandler.Loading())
            emit(DataHandler.Error(message = "EEE"))
        }
        chronicleRepository.getAllChronicles().test {
            val firstItem = awaitItem()
            assertThat(firstItem).isInstanceOf(DataHandler.Loading::class.java)

            val secondItem = awaitItem()
            assertThat(secondItem).isInstanceOf(DataHandler.Success::class.java)

            awaitComplete()
        }
    }

    @Test
    fun `invoke use case with invalid data should return error`() = runBlocking {
        // Arrange
        val saveChronicleModel = SaveChronicleModel(title = "", content = "Content")

       /* coEvery { chronicleRepository.createChronicle(saveChronicleModel) } returns flow {
            emit(DataHandler.Success(Unit))
        }*/

        val useCase = CreateChronicleUseCase(chronicleRepository)

        useCase(saveChronicleModel = saveChronicleModel).test {

            val secondItem = awaitItem()
            assertThat(secondItem).isInstanceOf(DataHandler.Error::class.java)

            awaitComplete()
        }

        coVerify(exactly = 0) { chronicleRepository.createChronicle(saveChronicleModel) }
    }

    @Test
    fun `invoke repository with invalid data should return error`() = runBlocking {

        val saveChronicleModel = SaveChronicleModel(title = "", content = "Content")

         coEvery { chronicleRepository.createChronicle(saveChronicleModel) } returns flow {
             emit(DataHandler.Error(message = ""))
         }

        chronicleRepository.createChronicle(saveChronicleModel =SaveChronicleModel(title = "", content = "Content")).test {
            val secondItem = awaitItem()
            assertThat(secondItem).isInstanceOf(DataHandler.Error::class.java)
            awaitComplete()
        }

        coVerify() { chronicleRepository.createChronicle(saveChronicleModel) }
    }

}