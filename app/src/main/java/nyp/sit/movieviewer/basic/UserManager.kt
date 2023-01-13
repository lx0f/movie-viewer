package nyp.sit.movieviewer.basic

import kotlinx.coroutines.*
import nyp.sit.movieviewer.basic.data.IUserRepository
import nyp.sit.movieviewer.basic.domain.AdminNumberExists
import nyp.sit.movieviewer.basic.domain.InvalidCredentials
import nyp.sit.movieviewer.basic.domain.LoginNameExists
import nyp.sit.movieviewer.basic.entity.User

class UserManager(private val repository: IUserRepository, private val setUser: (User) -> Unit) {

    private val userCodeHashMap = HashMap<String, Int>()
    var user: User? = null

    suspend fun sendCode(user: User) {
        val code = 1234
        delay(1000)
        userCodeHashMap[user.admin_number!!] = code
    }

    suspend fun verifyCode(user: User, code: Int): Boolean {
        val result = userCodeHashMap[user.admin_number!!] == code
        return if (result) {
            delay(1000)
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
                val adminNumberUserJob = async { repository.getUserByAdminNumber(user.admin_number!!) }
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
}