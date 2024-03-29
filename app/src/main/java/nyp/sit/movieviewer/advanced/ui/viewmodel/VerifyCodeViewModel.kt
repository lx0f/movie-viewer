package nyp.sit.movieviewer.advanced.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import nyp.sit.movieviewer.advanced.MovieApplication
import nyp.sit.movieviewer.advanced.UserManager
import nyp.sit.movieviewer.advanced.entity.User

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
