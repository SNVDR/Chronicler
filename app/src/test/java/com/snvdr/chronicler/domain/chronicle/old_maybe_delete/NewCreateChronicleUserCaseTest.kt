package com.snvdr.chronicler.domain.chronicle.old_maybe_delete

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.snvdr.chronicler.domain.chronicle.ChronicleRepository
import com.snvdr.chronicler.domain.chronicle.SaveChronicleModel
import com.snvdr.chronicler.domain.chronicle.use_cases.CreateChronicleUseCase
import com.snvdr.chronicler.utils.DataHandler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class NewCreateChronicleUserCaseTest {

    private val fakeRepository = mockk<ChronicleRepository>()
    private lateinit var createChronicleUseCase: CreateChronicleUseCase
    @Before
    fun setUp(){
        createChronicleUseCase = CreateChronicleUseCase(fakeRepository)
    }

    @Test
    fun `emit success if try to create chronicle with title`() = runBlocking{
        val saveChronicleModel = SaveChronicleModel(title = "s", content = "Content")
        coEvery { fakeRepository.createChronicle(saveChronicleModel) } returns flowOf(DataHandler.Success(Unit))
        createChronicleUseCase(saveChronicleModel).test {
            val emission = awaitItem()
            assertThat(emission).isInstanceOf(DataHandler.Success::class.java)
        }

       coVerify { fakeRepository.createChronicle(saveChronicleModel) }
    }

    @Test
    fun `emit error if try to create chronicle with empty title`() = runBlocking{
        val saveChronicleModel = SaveChronicleModel(title = "", content = "Content")
        createChronicleUseCase(saveChronicleModel).test {
            val emission = awaitItem()
            assertThat(emission).isInstanceOf(DataHandler.Error::class.java)
        }

        coVerify(exactly = 0) { fakeRepository.createChronicle(saveChronicleModel) }
    }

}