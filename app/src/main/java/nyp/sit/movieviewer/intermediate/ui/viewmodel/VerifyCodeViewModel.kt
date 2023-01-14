package nyp.sit.movieviewer.intermediate.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import nyp.sit.movieviewer.intermediate.MovieApplication
import nyp.sit.movieviewer.intermediate.UserManager
import nyp.sit.movieviewer.intermediate.entity.User

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
