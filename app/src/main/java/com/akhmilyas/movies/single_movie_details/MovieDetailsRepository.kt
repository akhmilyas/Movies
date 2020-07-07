package com.akhmilyas.movies.single_movie_details

import androidx.lifecycle.LiveData
import com.akhmilyas.movies.data.MovieDetails
import com.akhmilyas.movies.data.api.TheMovieDBInterface
import com.akhmilyas.movies.data.repository.MovieDetailsNetworkDataSource
import com.akhmilyas.movies.data.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository (private val apiService : TheMovieDBInterface) {

    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails (compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetails> {

        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService,compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }
}