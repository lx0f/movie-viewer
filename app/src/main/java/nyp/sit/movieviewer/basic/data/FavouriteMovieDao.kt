package nyp.sit.movieviewer.basic.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import nyp.sit.movieviewer.basic.entity.FavouriteMovie

@Dao
interface FavouriteMovieDao {
    @Query("SELECT * FROM FavouriteMovie WHERE user_login_name = :loginName")
    fun getAllFavouriteMovies(loginName: String): Flow<List<FavouriteMovie>>

    @Insert
    suspend fun addFavouriteMovie(favouriteMovie: FavouriteMovie)

    @Query("SELECT * FROM FavouriteMovie WHERE movie_title = :title LIMIT 1")
    suspend fun getFavouriteMovieByTitle(title: String): FavouriteMovie?

    @Query("SELECT EXISTS (SELECT 1 FROM FavouriteMovie WHERE user_login_name = :loginName AND movie_title = :title)")
    suspend fun checkMovieIsFavourite(loginName: String, title: String): Boolean
}