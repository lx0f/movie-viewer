package nyp.sit.movieviewer.basic

import android.app.Application
import nyp.sit.movieviewer.basic.data.IMovieRepository
import nyp.sit.movieviewer.basic.data.IUserRepository
import nyp.sit.movieviewer.basic.data.MovieRepository
import nyp.sit.movieviewer.basic.data.UserRepository

class MovieApplication : Application() {

    lateinit var userRepository: IUserRepository
    lateinit var movieRepository: IMovieRepository

    override fun onCreate() {
        super.onCreate()
        movieRepository = MovieRepository()
        userRepository = UserRepository()
    }
}