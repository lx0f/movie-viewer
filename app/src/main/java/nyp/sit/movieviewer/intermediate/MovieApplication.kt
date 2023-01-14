package nyp.sit.movieviewer.intermediate

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.room.Room
import com.amazonaws.auth.AWSBasicCognitoIdentityProvider
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserState.*
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobile.config.AWSConfiguration
import nyp.sit.movieviewer.intermediate.data.*
import nyp.sit.movieviewer.intermediate.entity.User
import nyp.sit.movieviewer.intermediate.ui.activity.MovieListActivity
import nyp.sit.movieviewer.intermediate.util.TheMovieDbUrlHelper
import java.lang.Exception

class MovieApplication : Application() {

    private lateinit var userRepository: IUserRepository
    lateinit var movieRepository: IMovieRepository
    lateinit var userManager: UserManager
    lateinit var database: MovieDatabase
    var user: User? = null

    override fun onCreate() {
        super.onCreate()
        TheMovieDbUrlHelper.registerApiKey(getString(R.string.api_key))
        database = Room.databaseBuilder(
            this.applicationContext,
            MovieDatabase::class.java,
            "moviedatabase"
        )
            .fallbackToDestructiveMigration()
            .build()
        movieRepository = MovieRepository(
            database.movieDao(),
            database.favouriteMovieDao(),
            MovieWebDataSource()
        )
        userRepository = UserRepository()

        // Instantiate AWSMobileClient
        AWSMobileClient.getInstance().initialize(
            this.applicationContext,
            AWSConfiguration(this.applicationContext, R.raw.awsconfiguration),
            object : Callback<UserStateDetails> {
                override fun onResult(result: UserStateDetails?) {
                    Log.d(TAG, result?.userState?.name ?: "AWSMobileClient Instantiation: Fail")
                    when (result?.userState) {
                        GUEST -> {}
                        // When signed in redirect to MovieListActivity
                        SIGNED_IN -> {
                            val intent = Intent(
                                this@MovieApplication.applicationContext,
                                MovieListActivity::class.java
                            )
                            startActivity(intent)
                        }
                        SIGNED_OUT -> {}
                        SIGNED_OUT_FEDERATED_TOKENS_INVALID -> {}
                        SIGNED_OUT_USER_POOLS_TOKENS_INVALID -> {}
                        UNKNOWN -> {}
                        null -> {}
                    }
                }

                override fun onError(e: Exception?) {
                    Log.e(TAG, e?.message.toString(), e)
                    if (e != null)
                        throw e
                }
            })
        userManager = UserManager(userRepository) { u: User? -> user = u }
    }

    companion object {
        val TAG: String? = MovieApplication::class.simpleName
    }
}