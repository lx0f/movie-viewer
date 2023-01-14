package nyp.sit.movieviewer.basic.util

import androidx.room.TypeConverter

class Converter {
    @TypeConverter
    fun fromString(value: String?): List<Int> {
        return value?.split(',')?.map { it.toInt() }
            ?: listOf()
    }

    @TypeConverter
    fun fromList(list: List<Int?>?): String {
        return list?.joinToString(','.toString())
            ?: ""
    }
}
