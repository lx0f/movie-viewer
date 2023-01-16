package nyp.sit.movieviewer.advanced.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBDocument
import kotlinx.serialization.Serializable
import java.io.Serializable as JioSerializable

@Serializable
@DynamoDBDocument
@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey
    var id: Int = 0,
    @ColumnInfo(name = "poster_path")
    var poster_path: String? = null,
    @ColumnInfo(name = "adult")
    var adult: Boolean? = null,
    @ColumnInfo(name = "overview")
    var overview: String? = null,
    @ColumnInfo(name = "release_date")
    var release_date: String? = null,
    @ColumnInfo(name = "genre_ids")
    var genre_ids: List<Int>? = null,
    @ColumnInfo(name = "original_title")
    var original_title: String? = null,
    @ColumnInfo(name = "original_language")
    var original_language: String? = null,
    @ColumnInfo(name = "title")
    var title: String? = null,
    @ColumnInfo(name = "backdrop_path")
    var backdrop_path: String? = null,
    @ColumnInfo(name = "popularity")
    var popularity: Double = 0.0,
    @ColumnInfo(name = "vote_count")
    var vote_count: Int = 0,
    @ColumnInfo(name = "video")
    var video: Boolean? = null,
    @ColumnInfo(name = "vote_average")
    var vote_average: Double = 0.0
) : JioSerializable