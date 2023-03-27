package com.example.movies.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.movies.R
import com.example.movies.model.remote.Movies
import com.example.movies.databinding.FragmentDetailsBinding
import com.example.movies.model.room.FavouriteData
import com.example.movies.viewmodel.DetailsFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private val viewModel: DetailsFragmentViewModel by viewModels()
    private lateinit var binding: FragmentDetailsBinding
    var favourites: Boolean = false
    var movieTitle = ""
    private lateinit var movieArg: Movies

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieArg = (requireArguments().getSerializable("value") as Movies)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        binding.movie = this.movieArg
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.refresh()

        observerViewModel()
        viewModel.fetch(movieArg.id!!)

        binding.favoriteIconDetails.setOnClickListener {
            if (favourites) {
                viewModel.deleteMovieData(movieArg.id!!)
                binding.favoriteIconDetails.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                favourites = false
            } else {
                viewModel.storeLocally(
                    FavouriteData(
                        movie_id = movieArg.id,
                        overview = movieArg.overview!!,
                        original_title = movieArg.original_title!!,
                        release_date = movieArg.release_date!!,
                        backdrop_path = movieArg.backdrop_path!!,
                        poster_path = movieArg.poster_path!!,
                        vote_average = movieArg.vote_average!!
                    )
                )
                favourites = true
                binding.favoriteIconDetails.setImageResource(R.drawable.ic_baseline_favorite_24)
            }
        }
    }

    fun observerViewModel() {
        viewModel.movieListLiveData.observe(viewLifecycleOwner) { movieData ->
            movieData?.let {
                movieTitle = it.original_title
                favourites = true
                binding.favoriteIconDetails.setImageResource(R.drawable.ic_baseline_favorite_24)
            }
        }

    }


}
