package nyp.sit.movieviewer.intermediate.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import nyp.sit.movieviewer.intermediate.entity.Movie

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie WHERE title = :title LIMIT 1")
    suspend fun getMovieByTitle(title: String): Movie?

    @Query("SELECT * FROM movie")
    suspend fun getAllMovies(): List<Movie>

    @Query("SELECT EXISTS (SELECT * FROM movie LIMIT 1)")
    suspend fun isNotEmpty(): Boolean

    @Insert
    suspend fun addMovie(movie: Movie)
}