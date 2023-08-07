package com.example.tvrawr.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tvrawr.R
import com.example.tvrawr.data.models.TvShow
import com.example.tvrawr.databinding.ItemTvShowBinding

class TvShowAdapter(private val onTvShowClick: (TvShow) -> Unit) :
    RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder>() {

    var tvShows: List<TvShow> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowViewHolder {
        val binding = ItemTvShowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TvShowViewHolder(binding, onTvShowClick)
    }

    override fun onBindViewHolder(holder: TvShowViewHolder, position: Int) {
        val tvShow = tvShows[position]
        holder.bind(tvShow)
    }



    override fun getItemCount(): Int = tvShows.size

    inner class TvShowViewHolder(
        private val binding: ItemTvShowBinding,
        private val onTvShowClick: (TvShow) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION }
                    ?: return@setOnClickListener
                onTvShowClick(tvShows[position])
            }
        }

        fun bind(tvShow: TvShow) {
            binding.tvShowName.text = tvShow.name
            binding.tvShowGenre.text = tvShow.genre
            binding.tvShowSynopsis.text = tvShow.synopsis  // Display the synopsis

            Glide.with(itemView)
                .load(tvShow.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(binding.tvShowImage)
        }
    }
}
