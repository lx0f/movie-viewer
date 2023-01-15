package nyp.sit.movieviewer.intermediate.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nyp.sit.movieviewer.intermediate.entity.User

class UserRepository : IUserRepository {
    private val users: ArrayList<User> = arrayListOf(User(
        // TODO: Delete this
        // Its for testing purposes
        "testuser",
        "testuser",
        "testuser@example.com",
        "testuser",
        "testuser"
    ))

    override fun getAllUsers(): Flow<ArrayList<User>> = flow {
        emit(users)
    }

    override suspend fun getUserByAdminNumber(adminNumber: String): User? {
         return users.firstOrNull { u -> u.admin_number == adminNumber }
    }

    override suspend fun getUserByLoginName(loginName: String): User? {
        return users.firstOrNull { u -> u.login_name == loginName }
    }

    override suspend fun addUser(user: User) {
        users.add(user)
    }
}