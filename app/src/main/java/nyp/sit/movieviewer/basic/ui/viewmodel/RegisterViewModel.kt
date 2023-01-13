package nyp.sit.movieviewer.basic.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Dispatchers
import nyp.sit.movieviewer.basic.MovieApplication
import nyp.sit.movieviewer.basic.UserManager
import nyp.sit.movieviewer.basic.domain.AdminNumberExists
import nyp.sit.movieviewer.basic.domain.LoginNameExists
import nyp.sit.movieviewer.basic.domain.RegisterStatus
import nyp.sit.movieviewer.basic.entity.User

class RegisterViewModel(
    private val userManager: UserManager
) : ViewModel() {

    fun submit(user: User): LiveData<RegisterStatus> = liveData(Dispatchers.IO) {
        emit(RegisterStatus.LOADING)
        try {
            userManager.register(user)
            emit(RegisterStatus.SUCCESS)
        } catch (err: Exception) {
            emit(RegisterStatus.FAIL)
            when (err) {
                is LoginNameExists -> emit(RegisterStatus.LOGIN_NAME_EXITS)
                is AdminNumberExists -> emit(RegisterStatus.ADMIN_NUMBER_EXISTS)
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
