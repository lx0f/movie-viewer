package nyp.sit.movieviewer.advanced.ui.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.paging.PagingData
import androidx.paging.cachedIn
import nyp.sit.movieviewer.advanced.MovieApplication
import nyp.sit.movieviewer.advanced.UserManager
import nyp.sit.movieviewer.advanced.data.IMovieRepository
import nyp.sit.movieviewer.advanced.domain.QueryType
import nyp.sit.movieviewer.advanced.entity.Movie

class MovieListViewModel(
    private val repository: IMovieRepository,
    private val userManager: UserManager
) : ViewModel() {

    private val _currentQuery = MutableLiveData(QueryType.POPULAR)

    var movies: LiveData<PagingData<Movie>> = _currentQuery.switchMap {
        repository.getMovieStream(it)
            .cachedIn(viewModelScope)
    }

    suspend fun signOut() {
        userManager.signOut()
    }

    fun changeQuery(queryType: QueryType) {
        _currentQuery.postValue(queryType)
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

        const val ITEMS_PER_PAGE = 20
    }
}
