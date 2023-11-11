package com.snvdr.chronicler.data.chronicle.database

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ChronicleDaoTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ChronicleDatabase
    private lateinit var dao: ChronicleDao
    @Before
    fun setUp(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ChronicleDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.getChronicleDao()
    }
    @Test
    fun insertChronicleItem() = runBlocking {
        val chronicle = ChronicleDbEntity(
            id = 1488,
            title = "Fake title", content = "Fake content"
        )
        dao.insertChronicle(chronicle = chronicle)

        val allChronicles = dao.getChronicles()

        assertThat(allChronicles).contains(chronicle)
    }
    @Test
    fun getChronicleById() = runBlocking {
        val chronicle = ChronicleDbEntity(
            id = 1488,
            title = "Fake title", content = "Fake content"
        )
        dao.insertChronicle(chronicle = chronicle)

        val result = dao.getSpecificChronicle(id = chronicle.id)

        assertThat(result).isEqualTo(chronicle)
    }
    @Test
    fun getChronicleByNotExistingId() = runBlocking {
        val chronicle = ChronicleDbEntity(
            id = 1488,
            title = "Fake title", content = "Fake content"
        )
        dao.insertChronicle(chronicle = chronicle)

        val result = dao.getSpecificChronicle(id =1337)

        assertThat(result).isNull()
    }
    @Test
    fun deleteChronicleItem() = runBlocking {
        val chronicle = ChronicleDbEntity(
            id = 1488,
            title = "Fake title", content = "Fake content"
        )
        dao.insertChronicle(chronicle = chronicle)
        dao.deleteSpecificChronicle(chronicle = chronicle)
        val allChronicles = dao.getChronicles()

        assertThat(allChronicles).doesNotContain(chronicle)
    }
    @Test
    fun deleteChronicleByIdItem() = runBlocking {
        val chronicle = ChronicleDbEntity(
            id = 1488,
            title = "Fake title", content = "Fake content"
        )
        dao.insertChronicle(chronicle = chronicle)
        dao.deleteSpecificChronicleById(id = chronicle.id)
        val allChronicles = dao.getChronicles()

        assertThat(allChronicles).doesNotContain(chronicle)
    }
    @Test
    fun deleteAllChronicleItems() = runBlocking {
        val chronicle = ChronicleDbEntity(
            id = 1488,
            title = "Fake title", content = "Fake content"
        )
        dao.insertChronicle(chronicle = chronicle)
        dao.deleteAllChronicles()
        val allChronicles = dao.getChronicles()

        assertThat(allChronicles).doesNotContain(chronicle)
    }
    @Test
    fun updateChronicleItem() = runBlocking {
        val chronicle = ChronicleDbEntity(
            id = 1488,
            title = "Fake title", content = "Fake content"
        )
        val updatedChronicle =  ChronicleDbEntity(
            id = 1488,
            title = "No Fake title", content = "No Fake content"
        )
        dao.insertChronicle(chronicle = chronicle)
        dao.updateChronicle(updatedChronicle)

        val result = dao.getSpecificChronicle(id = chronicle.id)

        assertThat(result.title).isEqualTo(updatedChronicle.title)
    }
    @Test
    fun getChroniclesBySearchQuery() = runBlocking {
        val chronicle1 = ChronicleDbEntity(
            id = 1488,
            title = "Fake title", content = "Fake content"
        )
        val chronicle2 = ChronicleDbEntity(
            id = 8841,
            title = "Title fake", content = "Content fake"
        )
        val chronicle3 = ChronicleDbEntity(
            id = 4188,
            title = "Title", content = "Content"
        )
        val testQuery = "%ake%"
        dao.insertChronicle(chronicle1)
        dao.insertChronicle(chronicle2)
        dao.insertChronicle(chronicle3)

        val result = dao.searchDatabase(query = testQuery)

        assertThat(result).doesNotContain(chronicle3)
    }
    @Test
    fun getChroniclesByDataDescendingOrder() = runBlocking {
        val chronicle1 = ChronicleDbEntity(
            id = 1488,
            title = "First t", content = "First c"
        )
        val chronicle2 = ChronicleDbEntity(
            id = 8841,
            title = "Second t", content = "Second c"
        )
        val chronicle3 = ChronicleDbEntity(
            id = 4188,
            title = "Third t", content = "Third c"
        )
        dao.insertChronicle(chronicle1)
        dao.insertChronicle(chronicle2)
        dao.insertChronicle(chronicle3)

        val result = dao.getLatestChronicles()
        val filteredList = result.sortedByDescending{ it.date }

        assertThat(result).isEqualTo(filteredList)
    }
    @Test
    fun getChroniclesByDateAscendingOrder() = runBlocking {
        val chronicle1 = ChronicleDbEntity(
            id = 1488,
            title = "First t", content = "First c"
        )
        val chronicle2 = ChronicleDbEntity(
            id = 8841,
            title = "Second t", content = "Second c"
        )
        val chronicle3 = ChronicleDbEntity(
            id = 4188,
            title = "Third t", content = "Third c"
        )
        dao.insertChronicle(chronicle1)
        dao.insertChronicle(chronicle2)
        dao.insertChronicle(chronicle3)

        val result = dao.getChroniclesWithOrderAndAsc("date")
        val filteredList = result.sortedBy{ it.date }

        assertThat(result).isEqualTo(filteredList)
    }
    @Test
    fun getChroniclesByTitleDescendingOrder() = runBlocking {
        val chronicle1 = ChronicleDbEntity(
            id = 1488,
            title = "First t", content = "First c"
        )
        val chronicle2 = ChronicleDbEntity(
            id = 8841,
            title = "Second t", content = "Second c"
        )
        val chronicle3 = ChronicleDbEntity(
            id = 4188,
            title = "Third t", content = "Third c"
        )
        dao.insertChronicle(chronicle1)
        dao.insertChronicle(chronicle2)
        dao.insertChronicle(chronicle3)

        val result = dao.getLatestChronicles()
        val filteredList = result.sortedByDescending{ it.title }

        assertThat(result).isEqualTo(filteredList)
    }
    @Test
    fun getChroniclesByTitleAscendingOrder() = runBlocking {
        val chronicle1 = ChronicleDbEntity(
            id = 1488,
            title = "First t", content = "First c"
        )
        val chronicle2 = ChronicleDbEntity(
            id = 8841,
            title = "Second t", content = "Second c"
        )
        val chronicle3 = ChronicleDbEntity(
            id = 4188,
            title = "Third t", content = "Third c"
        )
        dao.insertChronicle(chronicle1)
        dao.insertChronicle(chronicle2)
        dao.insertChronicle(chronicle3)

        val result = dao.getChroniclesWithOrderAndAsc("title")
        val filteredList = result.sortedBy{ it.date }

        assertThat(result).isEqualTo(filteredList)
    }
    @After
    fun teardown(){
        database.close()
    }
}