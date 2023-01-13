package nyp.sit.movieviewer.basic.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import nyp.sit.movieviewer.basic.domain.FavouriteMovieExists
import nyp.sit.movieviewer.basic.entity.FavouriteMovie
import nyp.sit.movieviewer.basic.entity.Movie
import nyp.sit.movieviewer.basic.entity.User

class MovieRepository(private val favouriteMovieDao: FavouriteMovieDao) : IMovieRepository {
    private val movies = MovieFixture.getAllMovies()

    // TODO: Change this to real database logic
    // This is meant to be temporary
    override fun getAllMovies(): Flow<List<Movie>> = flow {
        emit(movies)
    }

    override suspend fun addFavouriteMovie(user: User, movie: Movie) {
        val exists = favouriteMovieDao.checkMovieIsFavourite(user.login_name!!, movie.title!!)
        if (exists) {
            throw FavouriteMovieExists()
        }
        favouriteMovieDao.addFavouriteMovie(FavouriteMovie(movie.title!!, user.login_name!!))
    }

    override suspend fun getFavouriteMovieByTitle(title: String): FavouriteMovie? {
        return favouriteMovieDao.getFavouriteMovieByTitle(title)
    }

    override suspend fun getMovieByTitle(title: String): Movie? {
        return movies.firstOrNull { m -> m.title == title }
    }

    override suspend fun checkMovieIsFavourite(user: User, movie: Movie): Boolean {
        return favouriteMovieDao.checkMovieIsFavourite(user.login_name!!, movie.title!!)
    }

    override fun getAllFavouriteMovies(user: User): Flow<List<FavouriteMovie>> {
        return favouriteMovieDao.getAllFavouriteMovies(user.login_name!!)
    }

    override fun getAllFavouriteMoviesAsMovie(user: User): Flow<List<Movie>> {
        val favouriteMovies = favouriteMovieDao.getAllFavouriteMovies(user.login_name!!)
        val favouriteMoviesAsMovie = favouriteMovies.map { list ->
            list.map { f -> movies.first { m -> m.title == f.movie_title } }
        }
        return favouriteMoviesAsMovie
    }

}