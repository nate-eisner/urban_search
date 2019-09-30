package io.eisner.urban_search.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.eisner.urban_search.data.Repository
import io.eisner.urban_search.data.Sort
import io.reactivex.disposables.Disposables

class SearchViewModel(private val repository: Repository) : ViewModel() {
    private var _searchResult = MutableLiveData<SearchResult>()
    val searchResult: LiveData<SearchResult>
        get() = _searchResult
    private var _previousSearch = Disposables.disposed()

    fun search(word: CharSequence, currentSort: Sort) {
        _previousSearch.dispose()
        if (word.isEmpty()) {
            _searchResult.postValue(SearchResult.Data(emptyList()))
            return
        }
        val search = repository.searchFor(word.toString(), currentSort)
            .doOnError {
                _searchResult.postValue(SearchResult.Error)
            }.doOnSubscribe {
                _searchResult.postValue(SearchResult.Loading)
            }
        _previousSearch = search.subscribe {
            Log.d("UrbanSearch", "emit")
            _searchResult.postValue(SearchResult.Data(it))
        }
    }

    override fun onCleared() {
        _previousSearch.dispose()
        super.onCleared()
    }
}
