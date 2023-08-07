package com.example.tvrawr.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tvrawr.R
import com.example.tvrawr.data.models.TvShow
import com.example.tvrawr.databinding.FragmentAddTvShowBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTvShowFragment : Fragment() {

    private var _binding: FragmentAddTvShowBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TvShowViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTvShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAddTvShow.setOnClickListener {

            val name = binding.etTvShowName.text.toString()
            val genre = binding.etTvShowGenre.text.toString()
            val description = binding.etTvShowDescription.text.toString()
            val year = binding.etTvShowYear.text.toString()
            val imageUrl = binding.etTvShowImgUrl.text.toString()

            val errorMsg = validateTvShow(name, genre, year, description, imageUrl)
            if (errorMsg.isEmpty()) {
                val newShow = TvShow( name, "", genre, year, description, true,imageUrl)
                viewModel.insert(newShow)
                findNavController().popBackStack()
            } else {
                showAlert(errorMsg)
            }
        }
    }

    private fun validateTvShow(
        name: String,
        genre: String,
        year: String,
        description: String,
        imageUrl: String
    ): String {
        val errors = StringBuilder()

        if (name.isEmpty() || name.length > 100) {
            errors.append("${getString(R.string.err_show_name)}\n")
        }

        if (genre.isEmpty() || genre.length > 50) {
            errors.append("${getString(R.string.err_show_genre)}\n")
        }

        if (description.isEmpty() || description.length > 500) {
            errors.append("${getString(R.string.err_show_desc)}\n")
        }

        if (!year.matches(Regex("^(18[0-9]{2}|19[0-9]{2}|200[0-9]|201[0-9]|202[0-4])$"))) {
            errors.append("${getString(R.string.err_show_year)}\n")
        }

        if (imageUrl.isEmpty()) {
            errors.append("${getString(R.string.err_show_url)}\n")
        }

        return errors.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showAlert(message: String) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle(getString(R.string.btn_invalid_input_prompt))
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton(getString(R.string.btn_confirm), null)

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}