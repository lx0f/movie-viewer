package nyp.sit.movieviewer.basic.data

import kotlinx.coroutines.flow.Flow
import nyp.sit.movieviewer.basic.entity.Movie

interface IMovieRepository {
    fun getAllMovies(): Flow<ArrayList<Movie>>
}