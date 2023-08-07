package com.example.tvrawr.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tvrawr.R
import com.example.tvrawr.data.models.IMDBDetailEntry
import com.example.tvrawr.data.models.IMDBGenericEntry
import com.example.tvrawr.data.models.OMDbAPI
import com.example.tvrawr.data.models.TvShow
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SearchShowFragment : Fragment() {

    private lateinit var recyclerViewAdapter: TvShowAdapter
    private lateinit var searchResults: List<IMDBGenericEntry>
    private val tvShowViewModel: TvShowViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchButton: Button = view.findViewById(R.id.search_button)
        val searchEditText: EditText = view.findViewById(R.id.search_bar)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        val fabFavorites: FloatingActionButton = view.findViewById(R.id.fab_favorites)

        tvShowViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                AlertDialog.Builder(requireContext())
                    .setMessage(errorMessage)
                    .setPositiveButton("OK", null)
                    .show()
                tvShowViewModel.error.value = null // Reset the error message after it's displayed
            }
        }

        // Observe the searchResults LiveData
        tvShowViewModel.searchResults.observe(viewLifecycleOwner) { tvShows ->
            recyclerViewAdapter.tvShows = tvShows
            if (tvShows.isEmpty()) {
                Toast.makeText(context, "No results found", Toast.LENGTH_SHORT).show()
            }
        }

        // Initialize your adapter and pass the lambda to handle item clicks
        recyclerViewAdapter = TvShowAdapter { tvShow ->
            var updatedtvShow: TvShow
            tvShowViewModel.viewModelScope.launch {
                if (!tvShowViewModel.doesTvShowExist(tvShow.IMDbId)) {
                    val detailedTvShow = fetchByID(tvShow.IMDbId)
                    tvShow.synopsis = detailedTvShow?.Plot ?: ""
                    tvShow.genre = detailedTvShow?.Genre ?: ""
                    tvShowViewModel.insert(tvShow)
                    val action =
                        tvShow.id?.let {
                            SearchShowFragmentDirections.actionSearchShowFragmentToTvShowDetailFragment(
                                it
                            )
                        }
                    if (action != null) {
                        findNavController().navigate(action)
                    }
                } else {
                    updatedtvShow = tvShowViewModel.getTvShowSuspended(tvShow.IMDbId)
                    val action =
                        updatedtvShow.id?.let {
                            SearchShowFragmentDirections.actionSearchShowFragmentToTvShowDetailFragment(
                                it
                            )
                        }
                    if (action != null) {
                        findNavController().navigate(action)
                    }
                }


            }
        }
        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = recyclerViewAdapter
        searchButton.setOnClickListener {
            val searchTerm = searchEditText.text.toString().trim()
            if (searchTerm.length < 3) {
                Toast.makeText(context, "Search term must be at least 3 characters long", Toast.LENGTH_SHORT).show()
            } else if (searchTerm.isNotEmpty()) {
                fetchByFullText(searchTerm)
            } else {
                Toast.makeText(context, "Enter a search term", Toast.LENGTH_SHORT).show()
            }
        }


        fabFavorites.setOnClickListener {
            findNavController().navigate(R.id.action_searchShowFragment_to_landingPageFragment)
        }
    }

    private fun fetchByFullText(searchTerm: String) {
        tvShowViewModel.viewModelScope.launch {
            val results = searchIMDBbyFullText(searchTerm, 20) // Limit the results to 20
            searchResults = results ?: emptyList()
            withContext(Dispatchers.Main) {
                // Map the searchResults which are of type IMDBGenericEntry to TvShow objects.
                val tvShows = searchResults.map { imdbEntry ->
                    TvShow(
                        name = imdbEntry.Title,
                        IMDbId = imdbEntry.imdbID,
                        genre = "",
                        year = imdbEntry.Year,
                        synopsis = "",
                        imageUrl = imdbEntry.Poster,
                        isFavorite = false,
                        id = null // Do not manually set id
                    )
                }
                tvShowViewModel.searchResults.value = tvShows
            }
        }
    }


    private suspend fun searchIMDBbyFullText(searchTerm: String, limit: Int): List<IMDBGenericEntry>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = OMDbAPI.service.byFullText(searchTerm)
                response.Search.take(limit)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    tvShowViewModel.error.value = "Error fetching data"
                }
                null
            }
        }
    }

    private suspend fun fetchByID(imdbID: String): IMDBDetailEntry? {
        return withContext(Dispatchers.IO) {
            try {
                val response = OMDbAPI.service.byTitle(imdbID)
                response
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    tvShowViewModel.error.value = "Error fetching data"
                }
                null
            }
        }
    }

}
