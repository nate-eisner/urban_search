package io.eisner.urban_search.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.eisner.urban_search.data.Repository
import io.eisner.urban_search.data.Sort
import io.eisner.urban_search.observeOnce
import io.eisner.urban_search.testList
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mock<Repository>()
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        viewModel = SearchViewModel(mockRepository)
    }

    @Test
    fun doSuccessfulSearch() {
        whenever(mockRepository.searchFor(any(), any())).thenReturn(Flowable.just(testList))

        viewModel.search("test", Sort.ThumbsUp)

        viewModel.searchResult.observeOnce { result ->
            assert(result is SearchResult.Data)
            assertEquals(testList, (result as SearchResult.Data).data)
        }
    }

    @Test
    fun loadingState() {
        whenever(mockRepository.searchFor(any(), any())).thenReturn(
            Flowable.create(
                {},
                BackpressureStrategy.LATEST
            )
        )

        viewModel.search("test", Sort.ThumbsUp)

        viewModel.searchResult.observeOnce { result ->
            assert(result is SearchResult.Loading)
        }
    }
}