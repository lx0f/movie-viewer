package nyp.sit.movieviewer.intermediate.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewmodel.CreationExtras
import com.amazonaws.services.cognitoidentityprovider.model.InvalidPasswordException
import kotlinx.coroutines.Dispatchers
import nyp.sit.movieviewer.intermediate.MovieApplication
import nyp.sit.movieviewer.intermediate.UserManager
import nyp.sit.movieviewer.intermediate.domain.exception.AdminNumberExists
import nyp.sit.movieviewer.intermediate.domain.exception.LoginNameExists
import nyp.sit.movieviewer.intermediate.domain.status.RegisterStatus
import nyp.sit.movieviewer.intermediate.entity.User

class RegisterViewModel(
    private val userManager: UserManager
) : ViewModel() {

    fun submit(user: User): LiveData<RegisterStatus> = liveData(Dispatchers.IO) {
        emit(RegisterStatus.LOADING)
        try {
            userManager.registerWithCognito(user)
            emit(RegisterStatus.SUCCESS)
        } catch (err: Exception) {
            emit(RegisterStatus.FAIL)
            when (err) {
                is LoginNameExists -> emit(RegisterStatus.LOGIN_NAME_EXITS)
                is AdminNumberExists -> emit(RegisterStatus.ADMIN_NUMBER_EXISTS)
                is InvalidPasswordException -> emit(RegisterStatus.INVALID_PASSWORD)
            }
        }
    }

    companion object {
        val Factory = RegisterViewModel()

        class RegisterViewModel : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return RegisterViewModel(
                    (application as MovieApplication).userManager
                ) as T
            }
        }
    }
}
