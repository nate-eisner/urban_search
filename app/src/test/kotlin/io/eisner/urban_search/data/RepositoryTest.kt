package io.eisner.urban_search.data

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.eisner.urban_search.data.api.UrbanSearchApi
import io.eisner.urban_search.data.db.UrbanDatabase
import io.eisner.urban_search.data.db.UrbanDefinitionDao
import io.eisner.urban_search.data.model.UrbanDefinition
import io.eisner.urban_search.testList
import io.eisner.urban_search.testResponse
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RepositoryTest {
    private val mockDb = mock<UrbanDatabase>()
    private val mockDao = mock<UrbanDefinitionDao>()
    private val mockApi = mock<UrbanSearchApi>()
    private lateinit var testScheduler: TestScheduler
    private lateinit var repository: Repository
    @Before
    fun setup() {
        whenever(mockDb.urbanDefinitionDao()).thenReturn(mockDao)

        testScheduler = TestScheduler()
        repository = Repository(mockDb, mockApi, testScheduler, testScheduler)
    }

    @Test
    fun testDbAndApiSuccessSearch() {
        whenever(mockDao.getDefinition(any())).thenReturn(Maybe.just(testList))
        whenever(mockApi.searchFor(any())).thenReturn(Single.just(testResponse))
        val testSubscriber = repository.searchFor("testing", Sort.ThumbsUp).test()
        testScheduler.triggerActions()

        testSubscriber.assertValueCount(2)
        assertEquals(testList, testSubscriber.values()[0])
        assertEquals(testList, testSubscriber.values()[1])
        testSubscriber.assertComplete()
    }

    @Test
    fun testOnlyApiSuccessSearch() {
        whenever(mockDao.getDefinition(any())).thenReturn(Maybe.just(emptyList()))
        whenever(mockApi.searchFor(any())).thenReturn(Single.just(testResponse))
        val testSubscriber = repository.searchFor("testing", Sort.ThumbsUp).test()
        testScheduler.triggerActions()

        testSubscriber.assertValueCount(2)
        assertEquals(emptyList<UrbanDefinition>(), testSubscriber.values()[0])
        assertEquals(testList, testSubscriber.values()[1])
        testSubscriber.assertComplete()
    }

    @Test
    fun testDbSuccessApiErrorSearch() {
        whenever(mockDao.getDefinition(any())).thenReturn(Maybe.just(testList))
        whenever(mockApi.searchFor(any())).thenReturn(Single.error(Throwable("Something broken")))
        val testSubscriber = repository.searchFor("testing", Sort.ThumbsUp).test()
        testScheduler.triggerActions()

        assertEquals(testList, testSubscriber.values().last())
        testSubscriber.assertComplete()
    }
}