package com.example.movies.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.model.remote.Movies
import com.example.movies.databinding.ItemMoviesBinding

class MovieAdapter(private val clickAdapter: (Movies) -> Unit) :
    PagingDataAdapter<Movies, MovieAdapter.MovieViewHolder>(
        PlayersDiffCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {

        return MovieViewHolder(
            ItemMoviesBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    private class PlayersDiffCallback : DiffUtil.ItemCallback<Movies>() {
        override fun areItemsTheSame(oldItem: Movies, newItem: Movies): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movies, newItem: Movies): Boolean {
            return oldItem == newItem
        }
    }
    inner class MovieViewHolder(private val binding: ItemMoviesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Movies) {
            binding.root.setOnClickListener {
                clickAdapter.invoke(item)
            }
            binding.apply {
                movie = item
            }
        }
    }
}


