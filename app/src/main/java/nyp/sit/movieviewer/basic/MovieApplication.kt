package nyp.sit.movieviewer.basic

import android.app.Application
import nyp.sit.movieviewer.basic.data.IMovieRepository
import nyp.sit.movieviewer.basic.data.IUserRepository
import nyp.sit.movieviewer.basic.data.MovieRepository
import nyp.sit.movieviewer.basic.data.UserRepository
import nyp.sit.movieviewer.basic.entity.User

class MovieApplication : Application() {

    lateinit var userRepository: IUserRepository
    lateinit var movieRepository: IMovieRepository
    lateinit var userManager: UserManager
    var user: User? = null

    override fun onCreate() {
        super.onCreate()
        movieRepository = MovieRepository()
        userRepository = UserRepository()
        userManager = UserManager(userRepository) { u -> user = u }
    }
}