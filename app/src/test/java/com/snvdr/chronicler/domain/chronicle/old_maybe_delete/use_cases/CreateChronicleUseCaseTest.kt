package com.snvdr.chronicler.domain.chronicle.old_maybe_delete.use_cases

import com.google.common.truth.Truth.assertThat
import com.snvdr.chronicler.domain.chronicle.ChronicleRepository
import com.snvdr.chronicler.domain.chronicle.SaveChronicleModel
import com.snvdr.chronicler.domain.chronicle.old_maybe_delete.data.repository.FakeChronicleRepository
import com.snvdr.chronicler.domain.chronicle.use_cases.CreateChronicleUseCase
import com.snvdr.chronicler.utils.DataHandler
import junit.framework.Assert.assertTrue
import junit.framework.Assert.fail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class CreateChronicleUseCaseTest {

    lateinit var testRepository: ChronicleRepository
    lateinit var createChronicleUseCase: CreateChronicleUseCase
    val testMockitoRepo = mock<ChronicleRepository>()
    @Before
    fun setUp(){
        testRepository = FakeChronicleRepository()
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

}
