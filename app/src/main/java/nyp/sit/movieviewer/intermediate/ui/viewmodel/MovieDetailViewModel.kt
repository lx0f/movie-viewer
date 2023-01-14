package nyp.sit.movieviewer.intermediate.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.liveData
import androidx.lifecycle.viewmodel.CreationExtras
import nyp.sit.movieviewer.intermediate.MovieApplication
import nyp.sit.movieviewer.intermediate.data.IMovieRepository
import nyp.sit.movieviewer.intermediate.domain.status.AddFavouriteStatus
import nyp.sit.movieviewer.intermediate.domain.exception.FavouriteMovieExists
import nyp.sit.movieviewer.intermediate.entity.Movie
import nyp.sit.movieviewer.intermediate.entity.User
import nyp.sit.movieviewer.intermediate.util.TheMovieDbUrlHelper

class MovieDetailViewModel(private val repository: IMovieRepository, private val user: User) : ViewModel() {

    fun getPosterImageUrl(imagePath: String): Uri {
        return TheMovieDbUrlHelper.builImageUrl(imagePath)
    }

    suspend fun checkMovieIsFavourite(movie: Movie): Boolean {
        return repository.checkMovieIsFavourite(user, movie)
    }

    fun addAsFavourite(movie: Movie): LiveData<AddFavouriteStatus> = liveData {
        emit(AddFavouriteStatus.LOADING)
        try {
            repository.addFavouriteMovieWithDynamo(user, movie)
            emit(AddFavouriteStatus.SUCCESS)
        } catch (err: FavouriteMovieExists) {
            emit(AddFavouriteStatus.FAVOURITE_MOVIE_EXISTS)
        }
    }

    companion object {
        val Factory = MovieDetailViewModelFactory()

        class MovieDetailViewModelFactory : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return MovieDetailViewModel(
                    (application as MovieApplication).movieRepository,
                    application.user!!
                ) as T
            }
        }
    }

}
