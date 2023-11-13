package com.snvdr.chronicler.domain.chronicle.use_cases

import com.google.common.truth.Truth.assertThat
import com.snvdr.chronicler.domain.chronicle.ChronicleRepository
import com.snvdr.chronicler.domain.chronicle.SaveChronicleModel
import com.snvdr.chronicler.domain.chronicle.data.repository.NewFakeChronicleRepository
import com.snvdr.chronicler.utils.DataHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import org.mockito.Mockito
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class CreateChronicleUseCaseTest {

    lateinit var testRepository: ChronicleRepository
    lateinit var createChronicleUseCase: CreateChronicleUseCase
    val testMockitoRepo = mock<ChronicleRepository>()
    @Before
    fun setUp(){
        testRepository = NewFakeChronicleRepository()
        createChronicleUseCase = CreateChronicleUseCase(testRepository)
    }
    @Test
    fun `createChronicle with non-empty title should emit success`() = runBlocking {
        val saveChronicleModel = SaveChronicleModel(title = "Title", content = "Test Content")

        createChronicleUseCase(saveChronicleModel = saveChronicleModel).collect{
            // Assert
            when (it) {
                is DataHandler.Loading -> {
                    // Add any additional loading assertions if needed
                }
                is DataHandler.Error -> {
                    fail(it.message?:"Occurs error")
                }
                is DataHandler.Success -> {
                    assertTrue(true)
                }
            }
        }
    }

    @Test
    fun `createChronicle with empty title should emit error`() = runBlocking {
        val saveChronicleModel = SaveChronicleModel(title = "", content = "Test Content")

        // Act
        createChronicleUseCase(saveChronicleModel).collect {
            // Assert
            when (it) {
                is DataHandler.Loading -> {
                    // Add any additional loading assertions if needed
                }
                is DataHandler.Error -> {
                    // Add assertions for the error case
                    assertThat(it.message).isEqualTo("Title can't be empty")
                }
                is DataHandler.Success -> {
                    // Fail the test if it's a success state (optional)
                    fail("EXPECTED error BUT received success")
                }
            }
        }
    }

    @Test
    fun `mockito createChronicle with non-empty title should emit success`() = runBlocking {
        // Arrange
        val saveChronicleModel = SaveChronicleModel(title = "Title", content = "Test Content")

        Mockito.`when`(testMockitoRepo.createChronicle(saveChronicleModel)).thenReturn(flow {
            emit(DataHandler.Success(Unit))
        })
        val useCase = CreateChronicleUseCase(testMockitoRepo)
        // Act and Assert
        useCase.invoke(saveChronicleModel = saveChronicleModel).collect {
            when (it) {
                is DataHandler.Loading -> {
                    // Add any additional loading assertions if needed
                }
                is DataHandler.Error -> {
                    fail(it.message ?: "Occurs Error")
                    //assertTrue(true)
                }
                is DataHandler.Success -> {
                    assertTrue(true)
                }
            }
        }
    }

   @Test
    fun `mockito createChronicle with empty title should emit error`() = runBlocking {
        // Arrange
       val saveChronicleModel = SaveChronicleModel(title = "hhh", content = "Test Content")
       Mockito.`when`(testMockitoRepo.createChronicle(saveChronicleModel)).thenReturn(flow {
            emit(DataHandler.Error(message = ""))
        })
       val useCase = CreateChronicleUseCase(testMockitoRepo)

        // Act and Assert
       useCase.invoke(saveChronicleModel).collect {
            when (it) {
                is DataHandler.Loading -> {
                    // Add any additional loading assertions if needed
                }
                is DataHandler.Error -> {
                    assertThat(true)
                }
                is DataHandler.Success -> {
                    fail(it.message ?: "Occurs Success")
                }
            }
        }
    }

}
