package nyp.sit.movieviewer.basic

import android.app.Application
import androidx.room.Room
import nyp.sit.movieviewer.basic.data.*
import nyp.sit.movieviewer.basic.entity.Movie
import nyp.sit.movieviewer.basic.entity.User

class MovieApplication : Application() {

    private lateinit var userRepository: IUserRepository
    lateinit var movieRepository: IMovieRepository
    lateinit var userManager: UserManager
    var user: User? = null

    override fun onCreate() {
        super.onCreate()
        movieRepository = MovieRepository(MovieSqliteDatabase.getInstance(applicationContext))
        userRepository = UserRepository()
        userManager = UserManager(userRepository) { u: User? -> user = u }
    }
}