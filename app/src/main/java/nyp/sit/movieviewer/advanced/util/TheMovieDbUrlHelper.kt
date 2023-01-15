package nyp.sit.movieviewer.advanced.util

import android.net.Uri
import android.util.Log
import nyp.sit.movieviewer.advanced.domain.QueryType
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class TheMovieDbUrlHelper {
    companion object {

        private val TAG: String? = this::class.simpleName
        private const val BASE_URL = "https://api.themoviedb.org/3"
        private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p"
        private const val IMAGE_SIZE_PARAM = "w185"

        private const val MOVIE_PATH = "movie"
        private const val SEARCH_PATH = "search"

        private const val PAGE_PARAMETER = "page"
        private const val API_KEY_PARAMETER = "api_key"
        private const val QUERY_KEY_PARAMETER: String = "query"

        private lateinit var API_KEY: String

        fun registerApiKey(apiKey: String) {
            this.API_KEY = apiKey
        }

        fun buildUrl(queryType: QueryType, page: Int): URL {
            val urlString = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(MOVIE_PATH)
                .appendEncodedPath(queryType.value)
                .appendQueryParameter(API_KEY_PARAMETER, API_KEY)
                .appendQueryParameter(PAGE_PARAMETER, page.toString())
                .build()
                .toString()
            val url = URL(urlString)
            Log.d(TAG, "Built URL: $url")
            return url
        }

        fun builUrl(title: String): URL {
            val urlString = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(SEARCH_PATH)
                .appendEncodedPath(MOVIE_PATH)
                .appendQueryParameter(API_KEY_PARAMETER, API_KEY)
                .appendQueryParameter(QUERY_KEY_PARAMETER, title)
                .build()
                .toString()
            val url = URL(urlString)
            Log.d(TAG, "Built URL: $url")
            return url
        }

        @Suppress("BlockingMethodInNonBlockingContext")
        suspend fun responseFromUrl(url: URL): String? {
            val connection = url.openConnection() as HttpURLConnection
            try {
                val input = connection.inputStream
                val scanner = Scanner(input)
                scanner.useDelimiter("\\A")
                val hasInput = scanner.hasNext()
                return if (hasInput) {
                    scanner.next()
                } else {
                    null
                }
            } finally {
                connection.disconnect()
            }
        }

        fun builImageUrl(imagePath: String): Uri {
            return Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendEncodedPath(IMAGE_SIZE_PARAM)
                .appendEncodedPath(imagePath)
                .build()
        }
    }
}
