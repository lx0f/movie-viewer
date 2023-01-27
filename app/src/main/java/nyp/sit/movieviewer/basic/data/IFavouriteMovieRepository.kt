package nyp.sit.movieviewer.basic.data

import kotlinx.coroutines.flow.Flow
import nyp.sit.movieviewer.basic.entity.FavouriteMovie

interface IFavouriteMovieRepository {
    fun getAllFavouriteMovies(loginName: String): Flow<List<FavouriteMovie>>
    suspend fun addFavouriteMovie(favouriteMovie: FavouriteMovie)
    suspend fun getFavouriteMovieByTitle(title: String): FavouriteMovie?
    suspend fun checkMovieIsFavourite(loginName: String, title: String): Boolean
}