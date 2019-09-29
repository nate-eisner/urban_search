package io.eisner.urban_search.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import io.eisner.urban_search.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private val viewModel: SearchViewModel by viewModel()

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
        viewModel.searchResult.observe(this, Observer { results ->
            when (results) {
                is SearchResult.Data -> adapter.showResults(results.data)
                is SearchResult.Loading -> Log.d("UrbanSearch", "Loading...")
                is SearchResult.Error -> Log.e("UrbanSearch", "Error...")
            }
        })
    }
}
