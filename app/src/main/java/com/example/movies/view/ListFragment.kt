package com.example.movies.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.view.adapters.MovieAdapter
import com.example.movies.R
import com.example.movies.databinding.FragmentListBinding
import com.example.movies.model.remote.Movies
import com.example.movies.view.adapters.LoadingStateAdapter
import com.example.movies.viewmodel.ListFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : Fragment() {
    private val viewModel: ListFragmentViewModel by viewModels()
    private val movieAdapter = MovieAdapter { name: Movies ->
        clickItem(name)

    }
    private lateinit var binding: FragmentListBinding
    val movies: MutableList<Movies> = ArrayList()
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(layoutInflater)

        binding.movieListRv.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = movieAdapter
        }
        binding.toolbar.apply {
            title = "Movies"
            inflateMenu(R.menu.item_toolbar)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.favouriteMovies -> {
                        title = "Favourite Movies"
                        viewModel.fetch()
                        observerViewModelFavourites()
                    }

                    R.id.home -> {
                        viewModel.fetchNewMovies()
                        title = "Movies"
                    }
                }
                true
            }
        }
        binding.movieListRv.adapter = movieAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter { retry() }
        )
        movieAdapter.addLoadStateListener { loadState ->
            if (loadState.mediator?.refresh is LoadState.Loading) {
                if (movieAdapter.snapshot().isEmpty()) {
                    binding.loadingBar.isVisible = true
                }
                binding.listError.isVisible = false
            } else if (loadState.append.endOfPaginationReached) {
                if (movieAdapter.itemCount < 1) {
                    binding.loadingBar.visibility = View.GONE
                    binding.listError.visibility = View.VISIBLE
                } else {
                    binding.loadingBar.isVisible = false
                }
            } else {
                binding.loadingBar.isVisible = false

                val error = when {
                    loadState.mediator?.prepend is LoadState.Error -> loadState.mediator?.prepend as LoadState.Error
                    loadState.mediator?.append is LoadState.Error -> loadState.mediator?.append as LoadState.Error
                    loadState.mediator?.refresh is LoadState.Error -> loadState.mediator?.refresh as LoadState.Error

                    else -> null
                }
                error?.let {
                    if (movieAdapter.snapshot().isEmpty()) {
                        binding.listError.isVisible = true
                        binding.listError.text = it.error.localizedMessage
                    }
                }
            }
        }
        Toast.makeText(requireContext(), "Please wait...", Toast.LENGTH_SHORT).show()
        startSearchJob()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //viewModel.refresh()


//        binding.refreshLayout.setOnRefreshListener {
//            binding.movieListRv.visibility = View.GONE
//            binding.listError.visibility = View.GONE
//            binding.loadingBar.visibility = View.VISIBLE
//            //viewModel.refresh()
//            binding.refreshLayout.isRefreshing = false
//        }

        //observerViewModel()


    }

    private fun startSearchJob() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.fetchNewMovies().observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.loadingBar.visibility = View.GONE
                    Toast.makeText(requireContext(),"message " + it, Toast.LENGTH_SHORT).show()
                    movieAdapter.submitData(this@ListFragment.lifecycle, it)

                } else {
                    binding.loadingBar.visibility = View.GONE
                    binding.listError.visibility = View.VISIBLE
                }
            }
        }
    }
    /*private fun observerViewModel() {
        viewModel.movies.observe(viewLifecycleOwner) { movie ->
            movie?.let {
                binding.movieListRv.visibility = View.VISIBLE
                movieAdapter.submitList(movie as ArrayList<Movies>)
            }

        }
        viewModel.movieLoadingError.observe(viewLifecycleOwner) { isError ->
            isError?.let {
                binding.listError.visibility = if (it) View.VISIBLE else View.GONE

            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            isLoading?.let {
                binding.loadingBar.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    binding.listError.visibility = View.GONE
                    binding.movieListRv.visibility = View.GONE
                }
            }
        }
    }*/

    //observing the favourite movies
    private fun observerViewModelFavourites() {
        // clearing the list stored
        movies.clear()
        viewModel.movieListLiveData.observe(viewLifecycleOwner) { movie ->
            movie?.let {
                binding.movieListRv.visibility = View.VISIBLE

                for (e in movie) {
                    movies.add(
                        Movies(
                            id = e.movie_id,
                            overview = e.overview,
                            original_title = e.original_title,
                            release_date = e.release_date, backdrop_path = e.backdrop_path,
                            poster_path = e.poster_path, vote_average = e.vote_average
                        )
                    )
                }
            }

            // movieAdapter.submitList(movies)
        }
        viewModel.movieLoadingError.observe(viewLifecycleOwner, Observer { isError ->
            isError?.let {
                binding.listError.visibility = if (it) View.VISIBLE else View.GONE

            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                binding.loadingBar.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    binding.listError.visibility = View.GONE
                    binding.movieListRv.visibility = View.GONE
                }
            }
        })
    }

    // passing the data as bundle from the list activity to the details actvity
    private fun clickItem(data: Movies) {
        val bundle = Bundle()
        bundle.putSerializable("value", data)
        findNavController().navigate(R.id.detailsFragment, bundle)
    }

    private fun retry() {
        movieAdapter.retry()
    }
}
