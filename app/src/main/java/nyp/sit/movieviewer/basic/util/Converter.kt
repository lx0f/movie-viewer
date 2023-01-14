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

    companion object {
        fun fromString(value: String?): List<Int> {
            println(value)
            return value?.split(',')?.map { it.toInt() }
                ?: listOf()
        }

        fun fromList(list: List<Int?>?): String {
            return list?.joinToString(','.toString())
                ?: ""
        }
    }
}
