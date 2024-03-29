package nyp.sit.movieviewer.advanced.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
        binding.movieListProgressIndicator.isIndeterminate = false

        val j = lifecycleScope.launch {
            var progress = binding.movieListProgressIndicator.progress
            while (progress < 100) {
                delay(500)
                if (progress == 100) {
                    return@launch
                }
                progress += 5
                binding.movieListProgressIndicator.setProgressCompat(progress, true)
            }
        }

        viewModel.movies.observe(this@MovieListActivity) {
            lifecycleScope.launch(Dispatchers.IO) {
                j.cancel()
                binding.movieListProgressIndicator.setProgressCompat(100, true)
                adapter.submitData(it)
            }
        }

        val tab = binding.tabLayout.getTabAt(0)
        binding.tabLayout.selectTab(tab)
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "Favourites" -> {
                        val intent = Intent(this@MovieListActivity, FavouriteMovieListActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "Home" -> {
                        adapter.refresh()
                        binding.movieList.smoothScrollToPosition(0)
                    }
                }
            }
        })

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

