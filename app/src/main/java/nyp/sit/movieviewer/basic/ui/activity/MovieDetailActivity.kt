package nyp.sit.movieviewer.basic.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import nyp.sit.movieviewer.basic.R
import nyp.sit.movieviewer.basic.databinding.ActivityMovieDetailBinding
import nyp.sit.movieviewer.basic.entity.Movie

class MovieDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityMovieDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        val movie = intent.getParcelableExtra<Movie>("movie")
        binding.apply {
            movieTitle.text = movie?.title ?: "Error"
            movieOverview.text = movie?.overview ?: "Error"
            movieLangauge.text = movie?.original_langauge ?: "Error"
            movieReleaseDate.text = movie?.release_date ?: "Error"
        }
        setContentView(binding.root)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

}
