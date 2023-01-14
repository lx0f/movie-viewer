package nyp.sit.movieviewer.intermediate.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import nyp.sit.movieviewer.intermediate.domain.QueryType
import nyp.sit.movieviewer.intermediate.domain.exception.FavouriteMovieExists
import nyp.sit.movieviewer.intermediate.entity.FavouriteMovie
import nyp.sit.movieviewer.intermediate.entity.Movie
import nyp.sit.movieviewer.intermediate.entity.User
import nyp.sit.movieviewer.intermediate.entity.UserData
import nyp.sit.movieviewer.intermediate.ui.MoviePagingSource
import nyp.sit.movieviewer.intermediate.ui.viewmodel.MovieListViewModel

class MovieRepository(
    private val movieDao: MovieDao,
    private val favouriteMovieDao: FavouriteMovieDao,
    private val webDataSource: MovieWebDataSource,
) : IMovieRepository {
    private val movies = arrayListOf<Movie>()
    override var ddbMapper: DynamoDBMapper? = null

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

    override fun getFavouriteMoviesWithDynamo(user: User): Flow<List<Movie>> = flow {
        val userData = ddbMapper?.load(UserData::class.java, user.login_name)
        emit(userData?.favouriteMovies ?: listOf())
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

    override suspend fun addFavouriteMovieWithDynamo(user: User, movie: Movie) {
        coroutineScope {
            launch(Dispatchers.IO) {
                val id = user.login_name
                try {
                    var userData = ddbMapper?.load(UserData::class.java, id)
                    if (userData == null) {
                        userData = UserData(id!!, arrayListOf(movie))
                    } else {
                        userData.favouriteMovies.add(movie)
                    }
                    ddbMapper?.save(userData)
                } catch (err: Exception) {
                    Log.e(TAG, err.message.toString())
                    throw err
                }
            }
        }
    }



    companion object {
        val TAG: String? = MovieRepository::class.simpleName
    }

}