package nyp.sit.movieviewer.basic.ui.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import nyp.sit.movieviewer.basic.MovieApplication
import nyp.sit.movieviewer.basic.UserManager
import nyp.sit.movieviewer.basic.entity.User

class VerifyCodeViewModel(private val userManager: UserManager, private val user: User) : ViewModel() {

    suspend fun verify(code: Int): Boolean {
        return userManager.verifyCode(user, code)
    }

    companion object {
        val Factory = VerifyCodeViewModelFactory()

        class VerifyCodeViewModelFactory : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return VerifyCodeViewModel(
                    (application as MovieApplication).userManager,
                    application.user!!
                ) as T
            }
        }
    }

}
