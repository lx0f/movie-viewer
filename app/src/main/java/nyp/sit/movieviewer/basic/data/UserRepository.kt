package nyp.sit.movieviewer.basic.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

    override suspend fun getUserByAdminNumber(adminNumber: String): User? {
        delay(1000)
        return users.firstOrNull { u -> u.admin_number == adminNumber }
    }

    override suspend fun getUserByLoginName(loginName: String): User? {
        delay(1000)
        return users.firstOrNull { u -> u.login_name == loginName }
    }

    override suspend fun addUser(user: User) {
        // TODO: Verify password strength
        delay(1000)
        users.add(user)
    }
}