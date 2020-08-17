package io.eisner.urban_search.ui.search

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import io.eisner.urban_search.R
import io.eisner.urban_search.data.model.Track

class SearchRecyclerAdapter : RecyclerView.Adapter<SearchRecyclerAdapter.DefinitionViewHolder>() {
    var list: List<Track> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefinitionViewHolder {
        return DefinitionViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_definition,
                parent,
                false
            )
        )
    }

    fun showResults(newList: List<Track>) {
        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val nameArtist = newList[newItemPosition].name + newList[newItemPosition].artist
                val oldNameArtist = list[oldItemPosition].name + list[oldItemPosition].artist
                return nameArtist == oldNameArtist
            }

            override fun getOldListSize(): Int {
                return list.size
            }

            override fun getNewListSize(): Int {
                return newList.size
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val nameArtist = newList[newItemPosition].name + newList[newItemPosition].artist
                val oldNameArtist = list[oldItemPosition].name + list[oldItemPosition].artist
                return nameArtist == oldNameArtist
            }
        })
        result.dispatchUpdatesTo(this)
        list = newList
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DefinitionViewHolder, position: Int) {
        holder.bind(list[position])
    }

    class DefinitionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(track: Track) {
            with(itemView) {
                setOnClickListener {
                    val page: Uri = Uri.parse(track.url)
                    val intent = Intent(Intent.ACTION_VIEW, page)
                    context.startActivity(intent)
                }
                track.image.firstOrNull() { it.size == "large" }?.let {
                    findViewById<ImageView>(R.id.trackImage).load(it.url)
                }
                findViewById<TextView>(R.id.definition).text = "${track.artist} - ${track.name}"
                findViewById<TextView>(R.id.listeners).text = "${track.listeners}"
            }
        }
    }
}