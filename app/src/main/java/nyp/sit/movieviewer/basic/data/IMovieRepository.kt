package nyp.sit.movieviewer.basic.data

import kotlinx.coroutines.flow.Flow
import nyp.sit.movieviewer.basic.entity.FavouriteMovie
import nyp.sit.movieviewer.basic.entity.Movie
import nyp.sit.movieviewer.basic.entity.User

interface IMovieRepository {
    fun getAllFavouriteMovies(user: User): Flow<List<FavouriteMovie>>
    fun getAllFavouriteMoviesAsMovie(user: User): Flow<List<Movie>>
    fun getAllMovies(): Flow<List<Movie>>
    suspend fun addFavouriteMovie(user: User, movie: Movie)
    suspend fun getFavouriteMovieByTitle(title: String): FavouriteMovie?
    suspend fun getMovieByTitle(title: String): Movie?
    suspend fun checkMovieIsFavourite(user: User, movie: Movie): Boolean
}