package nyp.sit.movieviewer.basic.entity

import android.os.Parcel
import android.os.Parcelable

data class Movie(
    var overview: String? = null,
    var release_date: String? = null,
    var original_langauge: String? = null,
    var title: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(overview)
        parcel.writeString(release_date)
        parcel.writeString(original_langauge)
        parcel.writeString(title)
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