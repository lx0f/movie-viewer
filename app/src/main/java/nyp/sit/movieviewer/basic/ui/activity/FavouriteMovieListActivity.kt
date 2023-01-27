package nyp.sit.movieviewer.basic.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import nyp.sit.movieviewer.basic.R
import nyp.sit.movieviewer.basic.databinding.ActivityFavouriteMovieListBinding
import nyp.sit.movieviewer.basic.entity.Movie
import nyp.sit.movieviewer.basic.ui.MovieListAdapter
import nyp.sit.movieviewer.basic.ui.viewmodel.FavouriteMovieListViewModel

class FavouriteMovieListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavouriteMovieListBinding
    private lateinit var adapter: MovieListAdapter
    private val viewModel: FavouriteMovieListViewModel by viewModels { FavouriteMovieListViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteMovieListBinding.inflate(layoutInflater)
        adapter = MovieListAdapter(this)
        binding.movieList.adapter = adapter

        val movieObserver = Observer<List<Movie>> { movies ->
            adapter.clear()
            adapter.addAll(movies)
        }
        viewModel.movies.observe(this, movieObserver)

        binding.movieList.setOnItemClickListener { adapterView, _, i, _ ->
            val movie = adapterView.adapter.getItem(i) as Movie
            val intent = Intent(this, MovieDetailActivity::class.java)
            intent.putExtra("movie", movie)
            startActivity(intent)
        }

        setContentView(binding.root)
    }

}