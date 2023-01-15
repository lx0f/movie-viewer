package nyp.sit.movieviewer.advanced.ui.activity

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
import kotlinx.coroutines.launch
import nyp.sit.movieviewer.advanced.R
import nyp.sit.movieviewer.advanced.databinding.ActivityMovieListBinding
import nyp.sit.movieviewer.advanced.domain.QueryType
import nyp.sit.movieviewer.advanced.entity.Movie
import nyp.sit.movieviewer.advanced.ui.MovieAdapter
import nyp.sit.movieviewer.advanced.ui.viewmodel.MovieListViewModel

class MovieListActivity : AppCompatActivity() {

    private val TAG: String? = this::class.simpleName
    private lateinit var binding: ActivityMovieListBinding
    private lateinit var adapter: MovieAdapter
    private val viewModel: MovieListViewModel by viewModels { MovieListViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        adapter = MovieAdapter(
            object : DiffUtil.ItemCallback<Movie>() {
                override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                    return oldItem == newItem
                }
            },
            object : MovieAdapter.OnItemClickListener {
                override fun onItemClick(movie: Movie) {
                    val intent = Intent(this@MovieListActivity, MovieDetailActivity::class.java)
                    intent.putExtra("movie", movie)
                    startActivity(intent)
                }
            }
        )


        binding.movieList.adapter = adapter

        viewModel.movies.observe(this@MovieListActivity) {
            lifecycleScope.launch(Dispatchers.IO) {
                adapter.submitData(it)
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
            "Sort By Popularity" -> viewModel.changeQuery(QueryType.POPULAR)
            "Sort By Ratings" -> viewModel.changeQuery(QueryType.TOP_RATED)
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

