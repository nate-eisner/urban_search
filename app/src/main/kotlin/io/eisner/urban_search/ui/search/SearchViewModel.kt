package io.eisner.urban_search.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.eisner.urban_search.data.Repository
import io.eisner.urban_search.data.Sort
import io.eisner.urban_search.data.Source
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: Repository) : ViewModel() {
    private var _searchResult = MutableLiveData<SearchResult>()
    val searchResult: LiveData<SearchResult>
        get() = _searchResult
    private var _previousSearch: Job? = null

    fun search(word: CharSequence, currentSort: Sort) {
        _previousSearch?.cancel()
        if (word.isEmpty()) {
            _searchResult.postValue(SearchResult.Data(Source.Remote, emptyList()))
            return
        }
        _previousSearch = viewModelScope.launch(Dispatchers.IO) {
            repository.searchFor(word.toString(), currentSort)
                .onStart { _searchResult.postValue(SearchResult.Loading) }
                .catch { _searchResult.postValue(SearchResult.Error) }
                .collect { _searchResult.postValue(SearchResult.Data(it.first, it.second)) }
        }
    }
}
