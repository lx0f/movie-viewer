package nyp.sit.movieviewer.intermediate.domain

enum class QueryType(val value: String) {
    LATEST("latest"),
    NOW_PLAYING("now_playing"),
    POPULAR("popular"),
    TOP_RATED("top_rated"),
    UPCOMING("upcoming"),
}
