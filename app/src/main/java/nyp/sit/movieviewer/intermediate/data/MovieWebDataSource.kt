package nyp.sit.movieviewer.intermediate.data

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import nyp.sit.movieviewer.intermediate.domain.QueryType
import nyp.sit.movieviewer.intermediate.entity.Movie
import nyp.sit.movieviewer.intermediate.util.TheMovieDbUrlHelper

class MovieWebDataSource {

    suspend fun getMoviesByPage(queryType: QueryType, page: Int): List<Movie> {
        val url = TheMovieDbUrlHelper.buildUrl(queryType, page)
        val responseJsonStr = TheMovieDbUrlHelper.responseFromUrl(url)!!
        val responseJson = Json.parseToJsonElement(responseJsonStr)
        val movieJsonArray = responseJson.jsonObject["results"]!!
        return Json.decodeFromJsonElement(movieJsonArray)
    }

    suspend fun getMovieByTitle(title: String): Movie? {
        val url = TheMovieDbUrlHelper.builUrl(title)
        val responseJsonStr = TheMovieDbUrlHelper.responseFromUrl(url)!!
        val responseJson = Json.parseToJsonElement(responseJsonStr)
        val movieJsonArray = responseJson.jsonObject["results"]!!
        val movies = Json.decodeFromJsonElement<List<Movie>>(movieJsonArray)
        return if (movies.isEmpty()) null else movies[0]
    }

}