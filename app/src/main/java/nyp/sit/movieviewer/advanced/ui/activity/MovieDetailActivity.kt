package nyp.sit.movieviewer.advanced.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nyp.sit.movieviewer.advanced.R
import nyp.sit.movieviewer.advanced.databinding.ActivityMovieDetailBinding
import nyp.sit.movieviewer.advanced.domain.status.AddFavouriteStatus
import nyp.sit.movieviewer.advanced.entity.Movie
import nyp.sit.movieviewer.advanced.ui.viewmodel.MovieDetailViewModel

class MovieDetailActivity : AppCompatActivity() {

    private val TAG: String? = this::class.simpleName
    private lateinit var binding: ActivityMovieDetailBinding
    lateinit var movie: Movie
    private val viewModel: MovieDetailViewModel by viewModels { MovieDetailViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        movie = intent.getParcelableExtra("movie")!!
        binding.apply {
            Picasso.get()
                .load(viewModel.getPosterImageUrl(movie.poster_path ?: ""))
                .into(binding.posterIV)
            movieOverview.text = movie.overview ?: "Error"
            movieLanguage.text = movie.original_language ?: "Error"
            movieReleaseDate.text = movie.release_date ?: "Error"
            movieHasvideo.text = movie.video.toString()
            movieIsAdult.text = movie.adult.toString()
            moviePopularity.text = movie.popularity.toString()
            movieVoteAvg.text = movie.vote_average.toString()
            movieVoteCount.text = movie.vote_count.toString()
        }
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            val isFavourite = viewModel.checkMovieIsFavourite(movie)
            if (!isFavourite) {
                runOnUiThread {
                    menuInflater.inflate(R.menu.movie_detail_menu, menu)
                }
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.title) {
            "Add As Favourite" -> {
                addAsFavourite()
            }
            else -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addAsFavourite() {
        val statusObserver = Observer<AddFavouriteStatus> { status: AddFavouriteStatus? ->
            when (status) {
                AddFavouriteStatus.LOADING, null -> Log.d(TAG, "LOADING...")
                AddFavouriteStatus.SUCCESS -> Log.d(TAG, "SUCCESS")
                AddFavouriteStatus.FAVOURITE_MOVIE_EXISTS -> Log.d(TAG, "FAVOURITE_MOVIE_EXITS")
            }
        }

        val status = viewModel.addAsFavourite(movie)
        status.observe(this, statusObserver)
    }
}
