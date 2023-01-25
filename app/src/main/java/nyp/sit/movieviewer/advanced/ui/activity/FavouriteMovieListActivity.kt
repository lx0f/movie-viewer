package nyp.sit.movieviewer.advanced.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_movie_list.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

        val moviesObserver = Observer<List<Movie>> {
            j.cancel()
            binding.movieListProgressIndicator.setProgressCompat(100, true)
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

        val tab = binding.tabLayout.getTabAt(1)
        binding.tabLayout.selectTab(tab)
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "Home" -> {
                        val intent = Intent(this@FavouriteMovieListActivity, MovieListActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        setContentView(binding.root)
    }

}