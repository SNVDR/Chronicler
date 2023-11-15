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


class GetChronicleByIdUseCaseTest {
    private lateinit var getChronicleByIdUseCase: GetChronicleByIdUseCase
    private val mockChronicleRepository = mockk<ChronicleRepository>()
    @Before
    fun setUp() {
        getChronicleByIdUseCase = GetChronicleByIdUseCase(mockChronicleRepository)
    }
    @Test
    fun `success return chronicleDto by id`() = runBlocking {
        val myId: Long = 1488
        val mockkModel = ChronicleDto(
            id = myId,
            title = "Title",
            content = "Content",
            date = LocalDateTime.now().toString()
        )
        coEvery { mockChronicleRepository.getSpecificChronicle(id = myId) } returns flowOf(
            DataHandler.Success(data = mockkModel)
        )

        // Act
        getChronicleByIdUseCase(myId).test {
            // Assert
            val item = awaitItem()
            assertThat(item).isInstanceOf(DataHandler.Success::class.java)
            assertThat((item as DataHandler.Success).data).isEqualTo(mockkModel)
            awaitComplete()
        }

        coVerify { mockChronicleRepository.getSpecificChronicle(id = myId) }
    }
    @Test
    fun `return error by passing invalid id` () = runBlocking {
        val invalidId:Long = 0

        getChronicleByIdUseCase(id = invalidId).test {
            val item = awaitItem()
            assertThat(item).isInstanceOf(DataHandler.Error::class.java)
            assertThat((item as DataHandler.Error).message).isEqualTo( "Id can't be 0 or less than")
            awaitComplete()
        }

        coVerify(exactly = 0) { mockChronicleRepository.getSpecificChronicle(id = invalidId) }

    }
    @Test
    fun `id correct but return error because chronicle with this id doesn't exist`() = runBlocking {
        val myId: Long = 1488
        coEvery { mockChronicleRepository.getSpecificChronicle(id = myId) } returns flowOf(
            DataHandler.Error(message = "Chronicle with this id doesn't exist")
        )

        // Act
        getChronicleByIdUseCase(myId).test {
            // Assert
            val item = awaitItem()
            assertThat(item).isInstanceOf(DataHandler.Error::class.java)
            assertThat((item as DataHandler.Error).message).isEqualTo("Chronicle with this id doesn't exist")
            awaitComplete()
        }

        coVerify { mockChronicleRepository.getSpecificChronicle(id = myId) }
    }

}