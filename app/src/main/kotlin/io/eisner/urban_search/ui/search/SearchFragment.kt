package io.eisner.urban_search.ui.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.eisner.urban_search.R
import io.eisner.urban_search.data.Sort
import io.eisner.urban_search.data.Source
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModel()
    private var currentSort: Sort = Sort.ASC

    private val searchView: SearchView by lazy { requireActivity().findViewById<SearchView>(R.id.search_view) }
    private val loadingView: ProgressBar by lazy {
        requireActivity().findViewById<ProgressBar>(
            R.id.loading
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val recyclerView = requireActivity().findViewById<RecyclerView>(R.id.recycler)
        val adapter = SearchRecyclerAdapter()
        recyclerView.adapter = adapter

        observeResults(adapter)
        setupSearchView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sort_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_thumbs_up -> {
                currentSort = Sort.ASC
                viewModel.search(searchView.query, currentSort)
                true
            }
            R.id.sort_thumbs_down -> {
                currentSort = Sort.DSC
                viewModel.search(searchView.query, currentSort)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun observeResults(adapter: SearchRecyclerAdapter) {
        viewModel.searchResult.observe(viewLifecycleOwner, Observer { results ->
            when (results) {
                is SearchResult.Data -> {
                    if (results.source == Source.Remote ||
                        (results.source == Source.Local && results.data.isNotEmpty())
                    ) {
                        loadingView.isVisible = false
                    }
                    adapter.showResults(results.data)
                    Log.d("UrbanSearch", "Set Data...")
                }
                is SearchResult.Loading -> {
                    loadingView.isVisible = true
                    Log.d("UrbanSearch", "Loading...")
                }
                is SearchResult.Error -> {
                    loadingView.isVisible = false
                    Snackbar.make(
                        requireActivity().findViewById<CoordinatorLayout>(R.id.container),
                        R.string.error,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    Log.e("UrbanSearch", "Error...")
                }
            }
        })
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.d("UrbanSearch", "Searching for $newText...")
                viewModel.search(newText, Sort.DSC)
                return true
            }
        })
    }
}
