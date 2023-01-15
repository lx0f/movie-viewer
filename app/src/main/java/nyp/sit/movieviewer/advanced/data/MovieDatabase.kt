package nyp.sit.movieviewer.advanced.data

import androidx.room.*
import nyp.sit.movieviewer.advanced.entity.FavouriteMovie
import nyp.sit.movieviewer.advanced.entity.Movie
import nyp.sit.movieviewer.advanced.util.Converter

@Database(
    entities = [FavouriteMovie::class, Movie::class],
    version = 2,
)
@TypeConverters(Converter::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun favouriteMovieDao(): FavouriteMovieDao
    abstract fun movieDao(): MovieDao
}