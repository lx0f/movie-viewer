package nyp.sit.movieviewer.basic

import android.app.Application
import androidx.room.Room
import nyp.sit.movieviewer.basic.data.*
import nyp.sit.movieviewer.basic.entity.Movie
import nyp.sit.movieviewer.basic.entity.User
import nyp.sit.movieviewer.basic.util.TheMovieDbUrlHelper

class MovieApplication : Application() {

    private lateinit var userRepository: IUserRepository
    lateinit var movieRepository: IMovieRepository
    lateinit var userManager: UserManager
    lateinit var database: MovieDatabase
    var user: User? = null

    override fun onCreate() {
        super.onCreate()
        TheMovieDbUrlHelper.registerApiKey(getString(R.string.api_key))
        database = Room.databaseBuilder(
            this.applicationContext,
            MovieDatabase::class.java,
            "moviedatabase"
        )
            .fallbackToDestructiveMigration()
            .build()
        movieRepository = MovieRepository(
            database.movieDao(),
            database.favouriteMovieDao(),
            MovieWebDataSource()
        )
        userRepository = UserRepository()
        userManager = UserManager(userRepository) { u: User? -> user = u }
    }
}