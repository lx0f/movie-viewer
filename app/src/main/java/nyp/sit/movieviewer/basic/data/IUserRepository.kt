package nyp.sit.movieviewer.basic.data

import kotlinx.coroutines.flow.Flow
import nyp.sit.movieviewer.basic.entity.User

interface IUserRepository {
    fun getAllUsers(): Flow<ArrayList<User>>
    suspend fun getUserByAdminNumber(adminNumber: String): User?
    suspend fun getUserByLoginName(loginName: String): User?
    suspend fun addUser(user: User)
}