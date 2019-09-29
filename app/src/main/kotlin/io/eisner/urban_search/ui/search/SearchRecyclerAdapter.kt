package io.eisner.urban_search.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.eisner.urban_search.R
import io.eisner.urban_search.data.model.UrbanDefinition

class SearchRecyclerAdapter : RecyclerView.Adapter<SearchRecyclerAdapter.DefinitionViewHolder>() {
    var list: List<UrbanDefinition> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefinitionViewHolder {
        return DefinitionViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_definition,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DefinitionViewHolder, position: Int) {
        holder.bind(list[position])
    }

    class DefinitionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(definition: UrbanDefinition) {
            // TODO
        }
    }
}