package nyp.sit.movieviewer.basic.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nyp.sit.movieviewer.basic.domain.AdminNumberExists
import nyp.sit.movieviewer.basic.domain.LoginNameExists
import nyp.sit.movieviewer.basic.entity.User

class UserRepository : IUserRepository {
    private val users: ArrayList<User> = arrayListOf(User(
        // TODO: Delete this
        // Its for testing purposes
        "login",
        "password",
        "email",
        "admin",
        "pem"
    ))

    override fun getAllUsers(): Flow<ArrayList<User>> = flow {
        delay(1000)
        emit(users)
    }

    override suspend fun addUser(user: User) {
        // TODO: Verify password strength
        val loginNameExists = users.any { u -> u.login_name == user.login_name }
        val adminNumberExists = users.any { u -> u.admin_number == user.admin_number }

        if (loginNameExists) {
            throw LoginNameExists()
        }

        if (adminNumberExists) {
            throw AdminNumberExists()
        }

        delay(1000)
        users.add(user)
    }
}