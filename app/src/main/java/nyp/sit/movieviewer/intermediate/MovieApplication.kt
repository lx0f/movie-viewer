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
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import com.amazonaws.regions.Region
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
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
    lateinit var ddbMapper: DynamoDBMapper
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
        userRepository = UserRepository()

        // Instantiate AWSMobileClient
        userManager = UserManager(userRepository) { u: User? -> user = u }
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
                            val user = userManager.getUserFromCognito()
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
                    val client = AmazonDynamoDBClient(
                        AWSMobileClient.getInstance().credentials
                    )
                    client.setRegion(Region.getRegion("ap-southeast-1"))
                    ddbMapper = DynamoDBMapper(client)
                    movieRepository.ddbMapper = ddbMapper
                }

                override fun onError(e: Exception?) {
                    Log.e(TAG, e?.message.toString(), e)
                    if (e != null)
                        throw e
                }
            })
        movieRepository = MovieRepository(
            database.movieDao(),
            database.favouriteMovieDao(),
            MovieWebDataSource(),
        )
    }

    companion object {
        val TAG: String? = MovieApplication::class.simpleName
    }
}