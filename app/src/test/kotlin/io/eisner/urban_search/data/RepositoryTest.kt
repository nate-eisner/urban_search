package io.eisner.urban_search.data

import io.eisner.urban_search.data.api.UrbanSearchApi
import io.eisner.urban_search.data.db.UrbanDatabase
import io.eisner.urban_search.data.db.UrbanDefinitionDao
import io.eisner.urban_search.data.model.Track
import io.eisner.urban_search.testList
import io.eisner.urban_search.testResponse
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RepositoryTest {
    private val mockDb = mockk<UrbanDatabase>()
    private val mockDao = mockk<UrbanDefinitionDao>()
    private val mockApi = mockk<UrbanSearchApi>()
    private lateinit var repository: Repository

    @Before
    fun setup() {
        every { mockDb.urbanDefinitionDao() } returns mockDao
        repository = Repository(mockDb, mockApi)
    }

    @Test
    fun testDbAndApiSuccessSearch() {
        runBlockingTest {
            coEvery { mockDao.insertDefinitions(any()) } returns Unit
            coEvery { mockDao.getTracks(any()) } returns testList
            coEvery { mockApi.searchFor(any()) } returns testResponse
            val list = mutableListOf<Pair<Source, List<Track>>>()
            repository.searchFor("testing", Sort.DSC).toList(list)

            assertEquals(2, list.size)
            assertEquals(testList, list[0].second)
            assertEquals(testList, list[1].second)

        }
    }

    @Test
    fun testOnlyApiSuccessSearch() {
        runBlockingTest {
            coEvery { mockDao.insertDefinitions(any()) } returns Unit
            coEvery { mockDao.getTracks(any()) } returns emptyList()
            coEvery { mockApi.searchFor(any()) } returns testResponse
            val list = mutableListOf<Pair<Source, List<Track>>>()
            repository.searchFor("testing", Sort.DSC).toList(list)

            assertEquals(2, list.size)
            assertEquals(emptyList<Track>(), list[0].second)
            assertEquals(testList, list[1].second)
        }
    }

    @Test
    fun testDbSuccessApiErrorSearch() {
        runBlockingTest {
            coEvery { mockDao.insertDefinitions(any()) } returns Unit
            coEvery { mockDao.getTracks(any()) } returns testList
            coEvery { mockApi.searchFor(any()) } throws Throwable("Something broken")
            val list = mutableListOf<Pair<Source, List<Track>>>()
            repository.searchFor("testing", Sort.DSC).catch { }.toList(list)

            assertEquals(testList, list.last().second)
        }
    }
}