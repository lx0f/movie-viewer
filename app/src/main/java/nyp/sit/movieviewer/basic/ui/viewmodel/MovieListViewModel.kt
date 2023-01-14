package nyp.sit.movieviewer.basic.ui.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import nyp.sit.movieviewer.basic.MovieApplication
import nyp.sit.movieviewer.basic.UserManager
import nyp.sit.movieviewer.basic.data.IMovieRepository
import nyp.sit.movieviewer.basic.domain.QueryType
import nyp.sit.movieviewer.basic.entity.Movie

class MovieListViewModel(
    repository: IMovieRepository,
    private val userManager: UserManager
) : ViewModel() {

    private val query = QueryType.POPULAR

    val movies: Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = ITEMS_PER_PAGE, enablePlaceholders = false),
        pagingSourceFactory = { repository.getMoviePagingSource(query) }
    )
        .flow
        .cachedIn(viewModelScope)

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

        private const val ITEMS_PER_PAGE = 20
    }
}
