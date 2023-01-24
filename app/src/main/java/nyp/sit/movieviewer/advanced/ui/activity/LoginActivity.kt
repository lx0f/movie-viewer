package nyp.sit.movieviewer.advanced.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import nyp.sit.movieviewer.advanced.databinding.ActivityLoginBinding
import nyp.sit.movieviewer.advanced.domain.status.LoginStatus
import nyp.sit.movieviewer.advanced.ui.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private val TAG: String? = this::class.simpleName
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels { LoginViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.apply {
            loginButton.setOnClickListener {
                if (verifyFieldsNotEmpty()) {
                    submit()
                }
            }
            registerButton.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
        setContentView(binding.root)
    }

    private fun submit() {
        val statusObserve = Observer<LoginStatus> { status: LoginStatus? ->
            when (status) {
                LoginStatus.LOADING, null -> Log.d(TAG, "LOADING...")
                LoginStatus.SUCCESS -> {
                    Log.d(TAG, "SUCCESS")
                    val intent = Intent(this, MovieListActivity::class.java)
                    startActivity(intent)
                }
                LoginStatus.INVALID -> {
                    Log.d(TAG, "INVALID")
                    binding.apply {
                        val err = "Please check if your credentials are correct."
                        loginNameInputLayout.error = err
                        passwordInputLayout.error = err
                        loginNameInputLayout.isErrorEnabled = true
                        passwordInputLayout.isErrorEnabled = true
                    }
                }
            }
        }

        val loginName = binding.loginNameInput.text.toString()
        val password = binding.passwordInput.text.toString()
        val result = viewModel.submit(loginName, password)
        result.observe(this, statusObserve)
    }

    private fun verifyFieldsNotEmpty(): Boolean {
        var hasError = false
        binding.apply {
            if (loginNameInput.text?.isEmpty() == true) {
                loginNameInputLayout.error = "Please enter your login name"
                loginNameInputLayout.isErrorEnabled = true
                hasError = true
            }
            if (passwordInput.text?.isEmpty() == true) {
                passwordInputLayout.error = "Please enter your password"
                passwordInputLayout.isErrorEnabled = true
                hasError = true
            }
        }
        return !hasError
    }

}