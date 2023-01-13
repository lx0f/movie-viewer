package nyp.sit.movieviewer.basic.data

import androidx.room.Database
import androidx.room.RoomDatabase
import nyp.sit.movieviewer.basic.entity.FavouriteMovie

@Database(entities = [FavouriteMovie::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun favouriteMovieDao(): FavouriteMovieDao
}