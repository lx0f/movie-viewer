package nyp.sit.movieviewer.basic.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import nyp.sit.movieviewer.basic.domain.QueryType
import nyp.sit.movieviewer.basic.domain.exception.FavouriteMovieExists
import nyp.sit.movieviewer.basic.entity.FavouriteMovie
import nyp.sit.movieviewer.basic.entity.Movie
import nyp.sit.movieviewer.basic.entity.User
import nyp.sit.movieviewer.basic.ui.MoviePagingSource
import nyp.sit.movieviewer.basic.ui.viewmodel.MovieListViewModel

class MovieRepository(
    private val movieDao: MovieDao,
    private val favouriteMovieDao: FavouriteMovieDao,
    private val webDataSource: MovieWebDataSource
) : IMovieRepository {
    private val movies = arrayListOf<Movie>()

    // TODO: Change this to real database logic
    // This is meant to be temporary
    override fun getAllMovies(): Flow<List<Movie>> = flow {
        if (movieDao.isNotEmpty()) {
            emit(movieDao.getAllMovies())
        } else {
            emit(movies)
        }
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
        // check if movie is database
        val movieFromDb = movieDao.getMovieByTitle(title)
        if (movieFromDb != null)
            return movieFromDb

        // else retrieve from TheMovieDb
        val movieFromWeb = webDataSource.getMovieByTitle(title)

        // and persist it locally if not null
        if (movieFromWeb != null) {
            movieDao.addMovie(movieFromWeb)
        }

        return movieFromWeb
    }

    override suspend fun checkMovieIsFavourite(user: User, movie: Movie): Boolean {
        return favouriteMovieDao.checkMovieIsFavourite(user.login_name!!, movie.title!!)
    }

    override fun getAllFavouriteMovies(user: User): Flow<List<FavouriteMovie>> {
        return favouriteMovieDao.getAllFavouriteMovies(user.login_name!!)
    }

    @Suppress("UNCHECKED_CAST")
    override fun getAllFavouriteMoviesAsMovie(user: User): Flow<List<Movie>> {
        val favouriteMovies = favouriteMovieDao.getAllFavouriteMovies(user.login_name!!)
        val favouriteMoviesAsMovie = favouriteMovies.map { list ->
            list.map { f -> getMovieByTitle(f.movie_title) }
        }
        return favouriteMoviesAsMovie as Flow<List<Movie>>
    }

    override fun getMovieStream(queryType: QueryType): LiveData<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = MovieListViewModel.ITEMS_PER_PAGE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(webDataSource, queryType) }
        ).liveData
    }

}