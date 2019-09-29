package io.eisner.urban_search.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.eisner.urban_search.R
import io.eisner.urban_search.data.model.UrbanDefinition

class SearchRecyclerAdapter : RecyclerView.Adapter<SearchRecyclerAdapter.DefinitionViewHolder>() {
    var list: List<UrbanDefinition> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefinitionViewHolder {
        return DefinitionViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_definition,
                parent,
                false
            )
        )
    }

    fun showResults(list: List<UrbanDefinition>) {
        this.list = list
        // TODO can do diff here to improve UI updates
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DefinitionViewHolder, position: Int) {
        holder.bind(list[position])
    }

    class DefinitionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(definition: UrbanDefinition) {
            itemView.findViewById<TextView>(R.id.definition).text = definition.definition
            itemView.findViewById<TextView>(R.id.thumbs_down).text =
                definition.thumbsDown.toString()
            itemView.findViewById<TextView>(R.id.thumbs_up).text = definition.thumbsUp.toString()
        }
    }
}