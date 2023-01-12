package nyp.sit.movieviewer.basic.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nyp.sit.movieviewer.basic.entity.Movie
import java.lang.Thread.sleep

class MovieRepository : IMovieRepository {
    // TODO: Change this to real database logic
    // This is meant to be temporary
    override fun getAllMovies(): Flow<ArrayList<Movie>> {
        return flow {
            emit(MovieFixture.getAllMovies())
        }
    }
}