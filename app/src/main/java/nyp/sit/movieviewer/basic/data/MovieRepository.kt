package nyp.sit.movieviewer.basic.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nyp.sit.movieviewer.basic.domain.FavouriteMovieExists
import nyp.sit.movieviewer.basic.entity.FavouriteMovie
import nyp.sit.movieviewer.basic.entity.Movie
import nyp.sit.movieviewer.basic.entity.User

class MovieRepository : IMovieRepository {
    private val movies = MovieFixture.getAllMovies()
    private val favouriteMovies = ArrayList<FavouriteMovie>()

    // TODO: Change this to real database logic
    // This is meant to be temporary
    override fun getAllMovies(): Flow<List<Movie>> = flow {
        delay(1400)
        emit(movies)
    }

    override suspend fun addFavouriteMovie(user: User, movie: Movie) {
        if (favouriteMovies.any { f -> movie.title == f.movie_title }) {
            throw FavouriteMovieExists()
        }
        delay(1000)
        favouriteMovies.add(FavouriteMovie(movie.title, user.login_name))
    }

    override suspend fun getFavouriteMovieByTitle(title: String): FavouriteMovie? {
        delay(500)
        return favouriteMovies.firstOrNull { f -> f.movie_title == title }
    }

    override suspend fun getMovieByTitle(title: String): Movie? {
        delay(500)
        return movies.firstOrNull { m -> m.title == title }
    }

    override suspend fun checkMovieIsFavourite(user: User, movie: Movie): Boolean {
        delay(200)
        return favouriteMovies.any { f -> f.movie_title == movie.title && f.user_login_name == user.login_name }
    }

    override fun getAllFavouriteMovies(user: User): Flow<List<FavouriteMovie>> = flow {
        delay(1400)
        emit(favouriteMovies.filter { f -> user.login_name == f.user_login_name })
    }

    override fun getAllFavouriteMoviesAsMovie(user: User): Flow<List<Movie>> = flow {
        delay(800)
        val favouriteMovies = favouriteMovies.filter { f -> user.login_name == f.user_login_name }
        val movies = favouriteMovies.map { f -> movies.first { m -> m.title == f.movie_title } }
        emit(movies)
    }

}