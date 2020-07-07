package com.akhmilyas.movies.ui.single_movie_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.akhmilyas.movies.R
import com.akhmilyas.movies.data.MovieDetails
import com.akhmilyas.movies.data.api.POSTER_BASE_URL
import com.akhmilyas.movies.data.api.TheMovieDBClient
import com.akhmilyas.movies.data.api.TheMovieDBInterface
import com.akhmilyas.movies.data.repository.NetworkState
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_single_movie.*
import java.text.NumberFormat
import java.util.*

class SingleMovie : AppCompatActivity() {

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)

        val movieId: Int = intent.getIntExtra("id", 1)

        val apiService: TheMovieDBInterface = TheMovieDBClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer { movieDetails ->
            bindUI(movieDetails)
        })

        viewModel.networkState.observe(this, Observer { networkState ->
            details_movie_progress_bar.visibility =
                if (networkState == NetworkState.LOADING) View.VISIBLE else View.GONE
            details_movie_error.visibility =
                if (networkState == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    private fun bindUI(it: MovieDetails) {
        details_movie_title.text = it.title
        details_movie_tagline.text = it.tagline
        details_movie_release_date.text = it.releaseDate
        details_movie_rating.text = it.rating.toString()
        details_movie_overview.text = it.overview

        val runtime = it.runtime.toString() + " minutes"
        details_movie_runtime.text = runtime

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        details_movie_budget.text = formatCurrency.format(it.budget)
        details_movie_revenue.text = formatCurrency.format(it.revenue)

        val moviePosterURL = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(details_movie_background_img)
    }

    private fun getViewModel(movieId: Int): SingleMovieViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(movieRepository, movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }
}