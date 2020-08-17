package io.eisner.urban_search.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.eisner.urban_search.data.Repository
import io.eisner.urban_search.data.Sort
import io.eisner.urban_search.data.Source
import io.eisner.urban_search.observeOnce
import io.eisner.urban_search.testList
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<Repository>()

    @Test
    fun doSuccessfulSearch() {
        val viewModel = SearchViewModel(mockRepository)
        runBlockingTest {
            coEvery { mockRepository.searchFor(any(), any()) } returns flow {
                emit(Source.Remote to testList)
            }

            viewModel.search("test", Sort.ASC)

            viewModel.searchResult.observeOnce { result ->
                assert(result is SearchResult.Data)
                assertEquals(testList, (result as SearchResult.Data).data)
            }
        }
    }

    @Test
    fun loadingState() {
        val viewModel = SearchViewModel(mockRepository)
        runBlockingTest {
            coEvery { mockRepository.searchFor(any(), any()) } returns flow {
                emit(Source.Remote to emptyList())
            }
            viewModel.search("test", Sort.DSC)

            viewModel.searchResult.observeOnce { result ->
                assert(result is SearchResult.Loading)
            }
        }
    }
}