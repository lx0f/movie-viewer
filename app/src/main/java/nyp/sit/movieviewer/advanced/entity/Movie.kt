package nyp.sit.movieviewer.advanced.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBDocument
import kotlinx.serialization.Serializable
import nyp.sit.movieviewer.advanced.util.Converter

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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString(),
        Converter.fromString(parcel.readString()),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(poster_path)
        parcel.writeValue(adult)
        parcel.writeString(overview)
        parcel.writeString(release_date)
        parcel.writeString(Converter.fromList(genre_ids))
        parcel.writeString(original_title)
        parcel.writeString(original_language)
        parcel.writeString(title)
        parcel.writeString(backdrop_path)
        parcel.writeDouble(popularity)
        parcel.writeInt(vote_count)
        parcel.writeValue(video)
        parcel.writeDouble(vote_average)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }

}