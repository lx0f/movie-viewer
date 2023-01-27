package nyp.sit.movieviewer.basic.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nyp.sit.movieviewer.basic.entity.FavouriteMovie
import java.security.InvalidParameterException
import kotlin.collections.ArrayList

class MovieSqliteDatabase(
    context: Context
) : IFavouriteMovieRepository,
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "movie_viewer_database"
        const val DATABASE_VERSION = 1
        const val FAVOURITE_MOVIE_TABLE = "favourite_movie"
        const val TITLE = "title"
        const val TITLE_INDEX = 0
        const val USER_LOGIN_NAME = "user_login_name"
        const val USER_LOGIN_NAME_INDEX = 1

        private var INSTANCE: MovieSqliteDatabase? = null

        fun getInstance(context: Context? = null): MovieSqliteDatabase {
            return if (INSTANCE == null) {
                INSTANCE = MovieSqliteDatabase(context ?: throw InvalidParameterException())
                INSTANCE
            } else {
                INSTANCE
            } as MovieSqliteDatabase
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE $FAVOURITE_MOVIE_TABLE($TITLE TEXT,$USER_LOGIN_NAME TEXT, PRIMARY KEY ($TITLE, $USER_LOGIN_NAME))"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $FAVOURITE_MOVIE_TABLE")
        onCreate(db)
    }

    override fun getAllFavouriteMovies(loginName: String): Flow<List<FavouriteMovie>> = flow {
        val result = ArrayList<FavouriteMovie>()
        val query = "SELECT * FROM $FAVOURITE_MOVIE_TABLE WHERE $USER_LOGIN_NAME='$loginName'"
        val db = this@MovieSqliteDatabase.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(query, null)
        } catch (e: Exception) {
            println(e.stackTrace)
            emit(listOf())
            return@flow
        }

        var title: String
        var userLoginName: String

        if (cursor.moveToFirst()) {
            do {
                title = cursor.getString(TITLE_INDEX)
                userLoginName = cursor.getString(USER_LOGIN_NAME_INDEX)
                val movie = FavouriteMovie(
                    title,
                    userLoginName
                )

                result.add(movie)
                emit(result as List<FavouriteMovie>)
            } while (cursor.moveToNext())
        }
        db.close()
        return@flow
    }

    override suspend fun addFavouriteMovie(favouriteMovie: FavouriteMovie) {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(TITLE, favouriteMovie.movie_title)
        contentValues.put(USER_LOGIN_NAME, favouriteMovie.user_login_name)

        val result = db.insert(FAVOURITE_MOVIE_TABLE, null, contentValues)
        db.close()
    }

    override suspend fun getFavouriteMovieByTitle(title: String): FavouriteMovie? {
        var result: FavouriteMovie? = null
        val query = "SELECT * FROM $FAVOURITE_MOVIE_TABLE WHERE $TITLE='$title'"
        val db = this@MovieSqliteDatabase.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(query, null)
        } catch (e: Exception) {
            println(e.stackTrace)
            return null
        }

        val title: String
        val userLoginName: String

        if (cursor.moveToFirst()) {
            title = cursor.getString(TITLE_INDEX)
            userLoginName = cursor.getString(USER_LOGIN_NAME_INDEX)
            result = FavouriteMovie(
                title,
                userLoginName
            )
        }
        db.close()
        return result
    }

    override suspend fun checkMovieIsFavourite(loginName: String, title: String): Boolean {
        var result: FavouriteMovie? = null
        val query =
            "SELECT * FROM $FAVOURITE_MOVIE_TABLE WHERE $TITLE='$title' AND $USER_LOGIN_NAME='$loginName'"
        val db = this@MovieSqliteDatabase.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(query, null)
        } catch (e: Exception) {
            println(e.stackTrace)
            throw e
        }

        val movieTitle: String
        val userLoginName: String

        if (cursor.moveToFirst()) {
            movieTitle = cursor.getString(TITLE_INDEX)
            userLoginName = cursor.getString(USER_LOGIN_NAME_INDEX)
            result = FavouriteMovie(
                movieTitle,
                userLoginName
            )
        }
        db.close()
        return result != null
    }
}