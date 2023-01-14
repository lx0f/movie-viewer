package nyp.sit.movieviewer.basic.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import nyp.sit.movieviewer.basic.domain.QueryType
import nyp.sit.movieviewer.basic.entity.FavouriteMovie
import nyp.sit.movieviewer.basic.entity.Movie
import nyp.sit.movieviewer.basic.entity.User
import nyp.sit.movieviewer.basic.ui.MoviePagingSource

interface IMovieRepository {
    fun getAllMovies(): Flow<List<Movie>>
    suspend fun addFavouriteMovie(user: User, movie: Movie)
    suspend fun getFavouriteMovieByTitle(title: String): FavouriteMovie?
    suspend fun getMovieByTitle(title: String): Movie?
    suspend fun checkMovieIsFavourite(user: User, movie: Movie): Boolean
    fun getAllFavouriteMovies(user: User): Flow<List<FavouriteMovie>>
    @Suppress("UNCHECKED_CAST")
    fun getAllFavouriteMoviesAsMovie(user: User): Flow<List<Movie>>
    fun getMovieStream(queryType: QueryType): LiveData<PagingData<Movie>>
}