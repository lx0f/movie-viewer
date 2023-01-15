package nyp.sit.movieviewer.advanced.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import nyp.sit.movieviewer.advanced.databinding.ActivityFavouriteMovieListBinding
import nyp.sit.movieviewer.advanced.entity.Movie
import nyp.sit.movieviewer.advanced.ui.FavouriteMovieAdapter
import nyp.sit.movieviewer.advanced.ui.viewmodel.FavouriteMovieListViewModel

class FavouriteMovieListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavouriteMovieListBinding
    private lateinit var adapter: FavouriteMovieAdapter
    private val viewModel: FavouriteMovieListViewModel by viewModels { FavouriteMovieListViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteMovieListBinding.inflate(layoutInflater)
        adapter = FavouriteMovieAdapter(this)

        val moviesObserver = Observer<List<Movie>> {
            adapter.clear()
            adapter.addAll(it)
        }

        viewModel.movies.observe(this, moviesObserver)

        binding.movieList.adapter = adapter
        binding.movieList.setOnItemClickListener { parent, _, position, _ ->
            val intent = Intent(this, MovieDetailActivity::class.java)
            val movie = parent.adapter.getItem(position) as Movie
            intent.putExtra("movie", movie)
            startActivity(intent)
        }

        setContentView(binding.root)
    }

}