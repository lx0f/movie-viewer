package nyp.sit.movieviewer.basic.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import nyp.sit.movieviewer.basic.R
import nyp.sit.movieviewer.basic.databinding.ActivityMovieDetailBinding

class MovieDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityMovieDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
    }

}
