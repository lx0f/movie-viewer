package nyp.sit.movieviewer.basic.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Dispatchers
import nyp.sit.movieviewer.basic.MovieApplication
import nyp.sit.movieviewer.basic.UserManager
import nyp.sit.movieviewer.basic.data.IMovieRepository
import nyp.sit.movieviewer.basic.entity.Movie


class MovieListViewModel(
    repository: IMovieRepository,
    private val userManager: UserManager
) : ViewModel() {

    val movies: LiveData<List<Movie>> = repository.getAllMovies().asLiveData(Dispatchers.IO)

    suspend fun signOut() {
        userManager.signOut()
    }

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
                    (application as MovieApplication).movieRepository,
                    application.userManager
                ) as T
            }
        }
    }
}
