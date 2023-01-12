package nyp.sit.movieviewer.basic.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Dispatchers
import nyp.sit.movieviewer.basic.MovieApplication
import nyp.sit.movieviewer.basic.data.IMovieRepository
import nyp.sit.movieviewer.basic.entity.Movie


class MovieListViewModel(
    private val repository: IMovieRepository
) : ViewModel() {

    val movies: LiveData<ArrayList<Movie>> = repository.getAllMovies().asLiveData(Dispatchers.IO)

    companion object {
        val Factory = MovieListViewModelFactory()

        class MovieListViewModelFactory : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return MovieListViewModel(
                    (application as MovieApplication).movieRepository
                ) as T
            }
        }
    }
}
