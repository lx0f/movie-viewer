package nyp.sit.movieviewer.advanced.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Dispatchers
import nyp.sit.movieviewer.advanced.MovieApplication
import nyp.sit.movieviewer.advanced.data.IMovieRepository
import nyp.sit.movieviewer.advanced.entity.Movie
import nyp.sit.movieviewer.advanced.entity.User

class FavouriteMovieListViewModel(
    repository: IMovieRepository,
    user: User
) : ViewModel() {

    val movies: LiveData<List<Movie>> = repository.getFavouriteMoviesWithDynamo(user)
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