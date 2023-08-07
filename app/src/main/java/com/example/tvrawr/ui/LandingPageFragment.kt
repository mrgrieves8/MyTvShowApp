package com.example.tvrawr.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tvrawr.R
import com.example.tvrawr.data.models.TvShow
import com.example.tvrawr.databinding.FragmentLandingPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LandingPageFragment : Fragment() {

    private var _binding: FragmentLandingPageBinding? = null
    private val binding get() = _binding!!

    private val tvShowAdapter by lazy { TvShowAdapter(::navigateToTvShowDetail) }

    private val viewModel: TvShowViewModel by viewModels()

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLandingPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        viewModel.allFavoriteTvShows.observe(viewLifecycleOwner) { tvShows ->
            tvShowAdapter.tvShows = tvShows
            binding.tvNoFavorites.visibility = if (tvShows.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.fab.setOnClickListener {
            navController.navigate(R.id.action_landingPageFragment_to_addTvShowFragment)
        }

        binding.imgSearch.setOnClickListener {
            navController.navigate(R.id.action_landingPageFragment_to_searchShowFragment)
        }

        navController = Navigation.findNavController(view)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.rvTvShows.layoutManager = LinearLayoutManager(context)
        binding.rvTvShows.adapter = tvShowAdapter
    }

    private fun navigateToTvShowDetail(tvShow: TvShow) {
        val action = LandingPageFragmentDirections.actionLandingPageFragmentToTvShowDetailFragment(
            tvShow.id!!
        )
        navController.navigate(action)
    }
}
