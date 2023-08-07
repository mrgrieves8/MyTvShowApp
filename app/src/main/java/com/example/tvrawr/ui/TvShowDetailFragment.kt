package com.example.tvrawr.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.tvrawr.R
import com.example.tvrawr.databinding.FragmentTvShowDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowDetailFragment : Fragment() {
    private lateinit var binding: FragmentTvShowDetailBinding
    private val viewModel: TvShowViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTvShowDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvShowId = arguments?.getInt("tvShowId")

        // Delete button listener
        binding.btnDeleteTvShow.setOnClickListener {
            viewModel.selectedTvShow.value?.let { tvShow ->
                viewModel.removeFromFavorites(tvShow.id!!)
            }
        }

        binding.btnAddToFavorites.setOnClickListener {
            viewModel.selectedTvShow.value?.let { tvShow ->
                viewModel.addtoFavorites(tvShow.id!!)
            }
        }

        // observe selected tvShow
        viewModel.selectedTvShow.observe(viewLifecycleOwner) { tvShow ->
            tvShow?.let {
                binding.tvShowName.text = "${getString(R.string.tv_show_name)}: ${it.name}"
                binding.tvShowGenre.text = "${getString(R.string.tv_show_genre)}: ${it.genre}"
                binding.tvShowYear.text = "${getString(R.string.tv_show_year)}: ${it.year}"
                binding.tvShowSynopsis.text =
                    "${getString(R.string.tv_show_synopsis)}: ${it.synopsis}"

                // Load the image
                Glide.with(this)
                    .load(it.imageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .into(binding.tvShowImage)

                // Update button visibility
                observeFavoriteStatus(it.id!!)
            }
        }

        // if selected tvShow is null or not equal to the given id, fetch it
        if (viewModel.selectedTvShow.value?.id != tvShowId) {
            viewModel.getTvShow(tvShowId!!)?.observe(viewLifecycleOwner) { tvShow ->
                tvShow?.let {
                    viewModel.selectedTvShow.value = it
                }
            }
        }
    }

    private fun observeFavoriteStatus(tvShowId: Int) {
        viewModel.isFavorite(tvShowId).observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite == true) {
                binding.btnAddToFavorites.visibility = View.GONE
                binding.btnDeleteTvShow.visibility = View.VISIBLE
            } else {
                binding.btnDeleteTvShow.visibility = View.GONE
                binding.btnAddToFavorites.visibility = View.VISIBLE
            }
        }
    }
}

