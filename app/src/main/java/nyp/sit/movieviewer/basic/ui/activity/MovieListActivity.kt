package nyp.sit.movieviewer.basic.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import nyp.sit.movieviewer.basic.R
import nyp.sit.movieviewer.basic.databinding.ActivityMovieListBinding
import nyp.sit.movieviewer.basic.entity.Movie
import nyp.sit.movieviewer.basic.entity.User
import nyp.sit.movieviewer.basic.ui.MovieAdapter
import nyp.sit.movieviewer.basic.ui.viewmodel.MovieListViewModel

class MovieListActivity : AppCompatActivity() {

    private val TAG: String? = this::class.simpleName
    private lateinit var binding: ActivityMovieListBinding
    private lateinit var adapter: MovieAdapter
    private val viewModel: MovieListViewModel by viewModels { MovieListViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        adapter = MovieAdapter(object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }

        })

        binding.movieList.adapter = adapter

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.movies.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

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

