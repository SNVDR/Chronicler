package com.snvdr.chronicler.domain.chronicle.use_cases

import com.google.common.truth.Truth.assertThat
import com.snvdr.chronicler.domain.chronicle.ChronicleDto
import com.snvdr.chronicler.domain.chronicle.ChronicleOrder
import com.snvdr.chronicler.domain.chronicle.OrderType
import com.snvdr.chronicler.domain.chronicle.SaveChronicleModel
import com.snvdr.chronicler.domain.chronicle.data.repository.FakeChronicleRepository
import com.snvdr.chronicler.utils.DataHandler
import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking

import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import kotlin.random.Random

class GetAllChroniclesWithOrderTest {

    private lateinit var getAllChroniclesWithOrder: GetAllChroniclesWithOrder
    private lateinit var getChroniclesWithOrderUseCase: GetAllChroniclesWithOrder
    private lateinit var fakeRepository: FakeChronicleRepository
    private lateinit var faker: Faker

    @Before
    fun setUp() {
        fakeRepository = FakeChronicleRepository()
        getAllChroniclesWithOrder = GetAllChroniclesWithOrder(fakeRepository)
        getChroniclesWithOrderUseCase = GetAllChroniclesWithOrder(fakeRepository)
        faker = Faker()
        val fakeChroniclesToInsert = mutableListOf<SaveChronicleModel>()
        /*repeat(20) {

            fakeChroniclesToInsert.add(
                SaveChronicleModel(
                    title = faker.tolkien.characters(),
                    content = faker.tolkien.races()
                )
            )
        }
        fakeChroniclesToInsert.shuffle()
        runBlocking{
            fakeChroniclesToInsert.forEach { fakeRepository.createChronicle(it) }
        }*/
        fakeRepository.chronicles.add(
            ChronicleDto(
                Random.nextLong(), "A", "A",
                date = LocalDateTime.now().toString()
            )
        )
        fakeRepository.chronicles.add(
            ChronicleDto(
                Random.nextLong(), "B", "B",
                date = LocalDateTime.now().toString()
            )
        )
        fakeRepository.chronicles.add(
            ChronicleDto(
                Random.nextLong(), "C", "C",
                date = LocalDateTime.now().toString()
            )
        )
    }

    @Test
    fun `chronicles order by title ascending, correct order`() = runBlocking{
        val chronicles = getAllChroniclesWithOrder(ChronicleOrder.Title(OrderType.Ascending))
        /*chronicles.collect {dHandler->
            assertThat(dHandler).isInstanceOf(DataHandler.Success::class.java)
        }*/
        chronicles.collectLatest {
            when(it){
                is DataHandler.Error -> {

                }
                is DataHandler.Loading -> {
                }
                is DataHandler.Success -> {
                    val dataList = it.data
                    for (i in 0..dataList!!.size-2){
                        assertThat(dataList[i].title).isLessThan(dataList[i+1].title)
                    }
                }
            }
        }
    }
}