package nyp.sit.movieviewer.basic

import android.app.Application
import nyp.sit.movieviewer.basic.data.IMovieRepository
import nyp.sit.movieviewer.basic.data.MovieRepository

class MovieApplication : Application() {

    lateinit var movieRepository: IMovieRepository

    override fun onCreate() {
        super.onCreate()
        movieRepository = MovieRepository()
    }
}