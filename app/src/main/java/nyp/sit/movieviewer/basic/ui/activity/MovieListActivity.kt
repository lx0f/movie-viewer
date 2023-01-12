package nyp.sit.movieviewer.basic.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import nyp.sit.movieviewer.basic.R
import nyp.sit.movieviewer.basic.databinding.ActivityMovieListBinding
import nyp.sit.movieviewer.basic.entity.Movie
import nyp.sit.movieviewer.basic.ui.MovieListAdapter
import nyp.sit.movieviewer.basic.ui.viewmodel.MovieListViewModel

class MovieListActivity : AppCompatActivity() {

    lateinit var binding: ActivityMovieListBinding
    lateinit var adapter: MovieListAdapter
    private val viewModel: MovieListViewModel by viewModels { MovieListViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        adapter = MovieListAdapter(this, R.layout.list_item_movie)
        binding.apply {
            movieList.adapter = adapter
            movieList.setOnItemClickListener { adapterView, _, i, _ ->
                val movie = adapterView.getItemAtPosition(i) as Movie
                val intent = Intent(this@MovieListActivity, MovieDetailActivity::class.java)
                intent.putExtra("movie", movie)
                startActivity(intent)
            }
        }

        val moviesObserver = Observer<ArrayList<Movie>> { movies ->
            adapter.clear()
            adapter.addAll(movies)
        }

        viewModel.movies.observe(this, moviesObserver)

        setContentView(binding.root)
    }
}

