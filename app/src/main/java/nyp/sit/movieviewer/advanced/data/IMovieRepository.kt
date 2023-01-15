package nyp.sit.movieviewer.advanced.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import kotlinx.coroutines.flow.Flow
import nyp.sit.movieviewer.advanced.domain.QueryType
import nyp.sit.movieviewer.advanced.entity.FavouriteMovie
import nyp.sit.movieviewer.advanced.entity.Movie
import nyp.sit.movieviewer.advanced.entity.User

interface IMovieRepository {
    var ddbMapper: DynamoDBMapper?
    fun getAllMovies(): Flow<List<Movie>>
    suspend fun addFavouriteMovie(user: User, movie: Movie)
    suspend fun addFavouriteMovieWithDynamo(user: User, movie: Movie)
    suspend fun getFavouriteMovieByTitle(title: String): FavouriteMovie?
    suspend fun getMovieByTitle(title: String): Movie?
    suspend fun checkMovieIsFavourite(user: User, movie: Movie): Boolean
    fun getAllFavouriteMovies(user: User): Flow<List<FavouriteMovie>>
    @Suppress("UNCHECKED_CAST")
    fun getAllFavouriteMoviesAsMovie(user: User): Flow<List<Movie>>
    fun getFavouriteMoviesWithDynamo(user: User): Flow<List<Movie>>
    fun getMovieStream(queryType: QueryType): LiveData<PagingData<Movie>>
}