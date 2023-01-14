package nyp.sit.movieviewer.intermediate.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.liveData
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Dispatchers
import nyp.sit.movieviewer.intermediate.MovieApplication
import nyp.sit.movieviewer.intermediate.UserManager
import nyp.sit.movieviewer.intermediate.domain.exception.InvalidCredentials
import nyp.sit.movieviewer.intermediate.domain.status.LoginStatus

class LoginViewModel(private val userManager: UserManager) : ViewModel() {

    fun submit(loginName: String, password: String): LiveData<LoginStatus> =
        liveData(Dispatchers.IO) {
            emit(LoginStatus.LOADING)
            try {
                userManager.login(loginName, password)
                emit(LoginStatus.SUCCESS)
            } catch (err: InvalidCredentials) {
                emit(LoginStatus.INVALID)
            }
        }

    companion object {
        val Factory = LoginViewModelFactory()

        class LoginViewModelFactory : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return LoginViewModel(
                    (application as MovieApplication).userManager
                ) as T
            }
        }
    }

}
