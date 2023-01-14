package nyp.sit.movieviewer.intermediate.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Dispatchers
import nyp.sit.movieviewer.intermediate.MovieApplication
import nyp.sit.movieviewer.intermediate.data.IMovieRepository
import nyp.sit.movieviewer.intermediate.entity.Movie
import nyp.sit.movieviewer.intermediate.entity.User

class FavouriteMovieListViewModel(
    repository: IMovieRepository,
    user: User
) : ViewModel() {

    val movies: LiveData<List<Movie>> = repository.getAllFavouriteMoviesAsMovie(user)
        .asLiveData(Dispatchers.IO)

    companion object {
        val Factory = FavouriteMovieListViewModelFactory()

        class FavouriteMovieListViewModelFactory : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return FavouriteMovieListViewModel(
                    (application as MovieApplication).movieRepository,
                    application.user!!
                ) as T
            }
        }
    }

}