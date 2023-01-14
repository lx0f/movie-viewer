package nyp.sit.movieviewer.basic.data

import androidx.room.*
import nyp.sit.movieviewer.basic.entity.FavouriteMovie
import nyp.sit.movieviewer.basic.entity.Movie
import nyp.sit.movieviewer.basic.util.Converter

@Database(
    entities = [FavouriteMovie::class, Movie::class],
    version = 2,
)
@TypeConverters(Converter::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun favouriteMovieDao(): FavouriteMovieDao
    abstract fun movieDao(): MovieDao
}