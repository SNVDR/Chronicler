package com.snvdr.chronicler.domain.chronicle.use_cases

import com.google.common.truth.Truth.assertThat
import com.snvdr.chronicler.domain.chronicle.ChronicleRepository
import com.snvdr.chronicler.domain.chronicle.SaveChronicleModel
import com.snvdr.chronicler.utils.DataHandler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.fail

class CreateChronicleUseCaseTestMocck {
    // Mock your repository
    private val chronicleRepository: ChronicleRepository = mockk()

    // Create an instance of your use case
    private val useCase = CreateChronicleUseCase(chronicleRepository)
    @Test
    fun `invoke with valid data should call repository`() = runBlocking {
        // Arrange
        val saveChronicleModel = SaveChronicleModel(title = "S", content = "Content")

        // Mock repository behavior
        coEvery { chronicleRepository.createChronicle(saveChronicleModel) } returns flowOf(DataHandler.Success(Unit))

        // Act
        val result = useCase(saveChronicleModel)

        // Assert
        result.collect { dataHandler ->
            // Use Truth for assertions
            assertThat(dataHandler).isInstanceOf(DataHandler.Success::class.java)
        }

        // Verify that the repository function was called with the correct parameters
        coVerify { chronicleRepository.createChronicle(saveChronicleModel) }
    }
    @Test
    fun `invoke with invalid data should return error`() = runBlocking {
        // Arrange
        val saveChronicleModel = SaveChronicleModel(title = "", content = "")

       // coEvery { chronicleRepository.createChronicle(any()) } returns flowOf(DataHandler.Success(Unit))


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
}