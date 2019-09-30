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
import androidx.appcompat.widget.SearchView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.eisner.urban_search.R
import io.eisner.urban_search.data.Sort
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private val viewModel: SearchViewModel by viewModel()
    private var currentSort: Sort = Sort.ThumbsUp

    private val searchView: SearchView by lazy { activity!!.findViewById<SearchView>(R.id.search_view) }

    private lateinit var loadingHandler: Handler
    private val loadingRunnable = Runnable {
        Snackbar.make(
            activity!!.findViewById<CoordinatorLayout>(R.id.container),
            R.string.loading,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        loadingHandler = Handler(Looper.getMainLooper())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val recyclerView = activity!!.findViewById<RecyclerView>(R.id.recycler)
        val adapter = SearchRecyclerAdapter()
        recyclerView.adapter = adapter

        observeResults(adapter)
        setupSearchView(adapter)
    }

    override fun onDetach() {
        loadingHandler.removeCallbacks(loadingRunnable)
        super.onDetach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sort_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_thumbs_up -> {
                currentSort = Sort.ThumbsUp
                viewModel.search(searchView.query, currentSort)
                true
            }
            R.id.sort_thumbs_down -> {
                currentSort = Sort.ThumbsDown
                viewModel.search(searchView.query, currentSort)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun observeResults(adapter: SearchRecyclerAdapter) {
        viewModel.searchResult.observe(this, Observer { results ->
            when (results) {
                is SearchResult.Data -> {
                    loadingHandler.removeCallbacks(loadingRunnable)
                    adapter.showResults(results.data)
                    Log.d("UrbanSearch", "Set Data...")
                }
                is SearchResult.Loading -> {
                    loadingHandler.postDelayed(loadingRunnable, 1500)
                    Log.d("UrbanSearch", "Loading...")
                }
                is SearchResult.Error -> {
                    loadingHandler.removeCallbacks(loadingRunnable)
                    Snackbar.make(
                        activity!!.findViewById<CoordinatorLayout>(R.id.container),
                        R.string.error,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    Log.e("UrbanSearch", "Error...")
                }
            }
        })
    }

    private fun setupSearchView(adapter: SearchRecyclerAdapter) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.d("UrbanSearch", "Searching for $newText...")
                viewModel.search(newText, Sort.ThumbsUp)
                return true
            }
        })
    }
}
