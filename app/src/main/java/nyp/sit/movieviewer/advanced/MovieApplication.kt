package nyp.sit.movieviewer.advanced

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.room.Room
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserState.*
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobile.config.AWSConfiguration
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import com.amazonaws.regions.Region
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import nyp.sit.movieviewer.advanced.data.*
import nyp.sit.movieviewer.advanced.entity.User
import nyp.sit.movieviewer.advanced.ui.activity.MovieListActivity
import nyp.sit.movieviewer.advanced.util.TheMovieDbUrlHelper

class MovieApplication : Application() {

    private lateinit var userRepository: IUserRepository
    lateinit var movieRepository: IMovieRepository
    lateinit var userManager: UserManager
    private lateinit var database: MovieDatabase
    private lateinit var ddbMapper: DynamoDBMapper
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
        AWSMobileClient.getInstance().addUserStateListener {
            when (it.userState) {
                GUEST -> {}
                SIGNED_IN -> {
                    // get credentials
                    val credentials = AWSMobileClient.getInstance().credentials
                    val client = AmazonDynamoDBClient(credentials)
                    client.setRegion(Region.getRegion("ap-southeast-1"))

                    // setup ddbMapper
                    ddbMapper = DynamoDBMapper(client)
                    movieRepository.ddbMapper = ddbMapper
                }
                SIGNED_OUT -> {
                    // clear credentials
                    val config = AWSConfiguration(this.applicationContext, R.raw.awsconfiguration)
                    val credentialsProvider = CognitoCachingCredentialsProvider(this.applicationContext, config)
                    credentialsProvider.clear()
                }
                SIGNED_OUT_FEDERATED_TOKENS_INVALID -> {}
                SIGNED_OUT_USER_POOLS_TOKENS_INVALID -> {}
                UNKNOWN -> {}
                null -> {}
            }
        }
    }

    companion object {
        val TAG: String? = MovieApplication::class.simpleName
    }
}