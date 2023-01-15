package nyp.sit.movieviewer.advanced

import android.util.Log
import com.amazonaws.mobile.client.AWSMobileClient
import kotlinx.coroutines.*
import nyp.sit.movieviewer.advanced.data.IUserRepository
import nyp.sit.movieviewer.advanced.domain.exception.AdminNumberExists
import nyp.sit.movieviewer.advanced.domain.exception.InvalidCredentials
import nyp.sit.movieviewer.advanced.domain.exception.LoginNameExists
import nyp.sit.movieviewer.advanced.entity.User
import kotlin.Exception

class UserManager(private val repository: IUserRepository, private val setUser: (User?) -> Unit) {

    private val userCodeHashMap = HashMap<String, Int>()
    var user: User? = null

    private suspend fun sendCode(user: User) {
        val code = 1234
        userCodeHashMap[user.admin_number!!] = code
    }

    suspend fun verifyCode(user: User, code: Int): Boolean {
        val result = userCodeHashMap[user.admin_number!!] == code
        return if (result) {
            user.verified = true
            result
        } else {
            result
        }
    }

    suspend fun register(user: User) {
        coroutineScope {
            launch(Dispatchers.IO) {
                val loginNameUserJob = async { repository.getUserByLoginName(user.login_name!!) }
                val adminNumberUserJob =
                    async { repository.getUserByAdminNumber(user.admin_number!!) }
                val loginNameExists = loginNameUserJob.await() != null
                val adminNumberExists = adminNumberUserJob.await() != null

                if (loginNameExists) {
                    throw LoginNameExists()
                }
                if (adminNumberExists) {
                    throw AdminNumberExists()
                }

                val addUserJob = async { repository.addUser(user) }
                val sendCodeJob = async { sendCode(user) }

                setUser(user)

                awaitAll(addUserJob, sendCodeJob)
            }
        }
    }

    suspend fun registerWithCognito(user: User) {
        coroutineScope {
            launch(Dispatchers.IO) {
                val signUpJob = async {
                    AWSMobileClient.getInstance().signUp(
                        user.login_name,
                        user.password,
                        mapOf(
                            "email" to user.email,
                            "custom:AdminNumber" to user.admin_number,
                            "custom:PemGrp" to user.pem_group
                        ),
                        null,
                    )
                }
                try {
                    val result = signUpJob.await()
                    Log.d(TAG, "User sub: ${result?.userSub.toString()}")
                    Log.d(TAG, "Confirmation state:${result?.confirmationState.toString()}")
                    setUser(user)
                } catch (e: Exception) {
                    Log.e(TAG, e.message.toString(), e)
                    throw e
                }
            }
        }
    }

    suspend fun login(loginName: String, password: String) {
        coroutineScope {
            launch(Dispatchers.IO) {
                val user = repository.getUserByLoginName(loginName)
                if (user != null && user.password == password) {
                    setUser(user)
                } else {
                    throw InvalidCredentials()
                }
            }
        }
    }

    suspend fun loginWithCognito(loginName: String, password: String) {
        coroutineScope {
            launch(Dispatchers.IO) {
                val signInJob = async {
                    AWSMobileClient.getInstance().signIn(
                        loginName,
                        password,
                        null,
                    )
                }
                try {
                    val result = signInJob.await()
                    Log.d(TAG, result.signInState.name)
                } catch (e: Exception) {
                    Log.e(TAG, e.message.toString(), e)
                    throw e
                }
                val user = getUserFromCognito()
                setUser(user)
            }
        }
    }

    suspend fun signOut() {
        coroutineScope {
            launch(Dispatchers.IO) {
                setUser(null)
                AWSMobileClient.getInstance().signOut()
            }
        }
    }

    fun getUserFromCognito(): User {
        val attributes = AWSMobileClient.getInstance().userAttributes
        val username = AWSMobileClient.getInstance().username
        val user = User(
            login_name = username,
            password = null,
            email = attributes["email"],
            admin_number = attributes["custom:AdminNumber"],
            pem_group = attributes["custom:PemGrp"],
            verified = attributes["email_verified"] == "true", // NYP's Cognito setup auto-confirm new accounts
            id = attributes["sub"]
        )
        setUser(user)
        return user
    }

    companion object {
        val TAG: String? = UserManager::class.simpleName
    }
}