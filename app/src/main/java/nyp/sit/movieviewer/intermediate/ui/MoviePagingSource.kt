package nyp.sit.movieviewer.intermediate.ui

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nyp.sit.movieviewer.intermediate.data.MovieWebDataSource
import nyp.sit.movieviewer.intermediate.domain.QueryType
import nyp.sit.movieviewer.intermediate.entity.Movie

class MoviePagingSource(
    private val dataSource: MovieWebDataSource,
    private val queryType: QueryType
) : PagingSource<Int, Movie>() {

    private val TAG: String? = this::class.simpleName

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: 1
        return try {
            withContext(Dispatchers.IO) {
                val movies = dataSource.getMoviesByPage(queryType, position)
                val nextKey = if (movies.isEmpty()) {
                    null
                } else {
                    position + 1
                }
                LoadResult.Page(
                    data = movies,
                    prevKey = if (position == 1) null else position - 1,
                    nextKey = nextKey
                )
            }
        } catch (err: Exception) {
            throw err
            LoadResult.Error(err)
        }
    }
}