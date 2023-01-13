package nyp.sit.movieviewer.basic.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nyp.sit.movieviewer.basic.R
import nyp.sit.movieviewer.basic.databinding.ActivityMovieDetailBinding
import nyp.sit.movieviewer.basic.domain.status.AddFavouriteStatus
import nyp.sit.movieviewer.basic.entity.Movie
import nyp.sit.movieviewer.basic.ui.viewmodel.MovieDetailViewModel

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
            movieTitle.text = movie.title ?: "Error"
            movieOverview.text = movie.overview ?: "Error"
            movieLangauge.text = movie.original_langauge ?: "Error"
            movieReleaseDate.text = movie.release_date ?: "Error"
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
