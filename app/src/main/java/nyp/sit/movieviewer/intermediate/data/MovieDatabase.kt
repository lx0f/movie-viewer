package nyp.sit.movieviewer.intermediate.data

import androidx.room.*
import nyp.sit.movieviewer.intermediate.entity.FavouriteMovie
import nyp.sit.movieviewer.intermediate.entity.Movie
import nyp.sit.movieviewer.intermediate.util.Converter

@Database(
    entities = [FavouriteMovie::class, Movie::class],
    version = 2,
)
@TypeConverters(Converter::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun favouriteMovieDao(): FavouriteMovieDao
    abstract fun movieDao(): MovieDao
}