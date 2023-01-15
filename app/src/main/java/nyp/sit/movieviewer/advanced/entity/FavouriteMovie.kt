package nyp.sit.movieviewer.advanced.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["movie_title", "user_login_name"])
data class FavouriteMovie(
    @ColumnInfo(name = "movie_title")
    val movie_title: String,
    @ColumnInfo(name = "user_login_name")
    val user_login_name: String
)