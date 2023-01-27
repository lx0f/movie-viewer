package nyp.sit.movieviewer.basic.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import nyp.sit.movieviewer.basic.R
import nyp.sit.movieviewer.basic.databinding.ActivityMovieListBinding
import nyp.sit.movieviewer.basic.entity.Movie
import nyp.sit.movieviewer.basic.ui.MovieListAdapter
import nyp.sit.movieviewer.basic.ui.viewmodel.MovieListViewModel

class MovieListActivity : AppCompatActivity() {

    private val TAG: String? = this::class.simpleName
    private lateinit var binding: ActivityMovieListBinding
    private lateinit var adapter: MovieListAdapter
    private val viewModel: MovieListViewModel by viewModels { MovieListViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        adapter = MovieListAdapter(this)
        binding.apply {
            movieList.adapter = adapter
            movieList.setOnItemClickListener { adapterView, _, i, _ ->
                val movie = adapterView.getItemAtPosition(i) as Movie
                val intent = Intent(this@MovieListActivity, MovieDetailActivity::class.java)
                intent.putExtra("movie", movie)
                startActivity(intent)
            }
        }

        val moviesObserver = Observer<List<Movie>> { movies ->
            adapter.clear()
            adapter.addAll(movies)
        }

        viewModel.movies.observe(this, moviesObserver)

        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.movie_list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.title) {
            "View Favourites" -> {
                val intent = Intent(this, FavouriteMovieListActivity::class.java)
                startActivity(intent)
            }
            "Sign Out" -> {
                Log.d(TAG, "SIGN OUT")
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    viewModel.signOut()
                    val intent = Intent(this@MovieListActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

