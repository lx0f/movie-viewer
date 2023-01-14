package nyp.sit.movieviewer.intermediate.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import nyp.sit.movieviewer.intermediate.databinding.ActivityRegisterBinding
import nyp.sit.movieviewer.intermediate.domain.status.RegisterStatus
import nyp.sit.movieviewer.intermediate.domain.status.RegisterStatus.*
import nyp.sit.movieviewer.intermediate.entity.User
import nyp.sit.movieviewer.intermediate.ui.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private val TAG: String? = this::class.simpleName
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels { RegisterViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        binding.registerButton.setOnClickListener {
            if (verifyFieldsNotEmpty()) {
                submit()
            }
        }
        setContentView(binding.root)
    }

    private fun submit() {
        val statusObserver = Observer<RegisterStatus> { status: RegisterStatus? ->
            when (status) {
                FAIL -> Log.d(TAG, "FAIL")
                LOADING, null -> Log.d(TAG, "LOADING...")
                SUCCESS -> {
                    Log.d(TAG, "SUCCESS")
                    val intent = Intent(this, VerifyCodeActivity::class.java)
                    startActivity(intent)
                }
                LOGIN_NAME_EXITS -> {
                    binding.loginNameInput.error = "Enter a different login name."
                }
                ADMIN_NUMBER_EXISTS -> {
                    binding.adminNumberInput.error = "Enter a different admin number."
                }
                INVALID_PASSWORD -> {
                    binding.passwordInput.error = "Invalid password." +
                            "Minimum length of 8." +
                            "Require number." +
                            "Require uppercase letter." +
                            "Require lowercase letter."
                }
            }
        }

        val newUser: User
        binding.apply {
            newUser = User(
                login_name = loginNameInput.text.toString(),
                password = passwordInput.text.toString(),
                admin_number = adminNumberInput.text.toString(),
                pem_group = pemGroupInput.text.toString(),
                email = emailInput.text.toString()
            )
        }
        val status = viewModel.submit(newUser)
        status.observe(this, statusObserver)
    }

    private fun verifyFieldsNotEmpty(): Boolean {
        var hasError = false
        binding.apply {
            if (loginNameInput.text.isEmpty()) {
                loginNameInput.error = "Please enter your login name"
                hasError = true
            }
            if (passwordInput.text.isEmpty()) {
                passwordInput.error = "Please enter your password"
                hasError = true
            }
            if (adminNumberInput.text.isEmpty()) {
                adminNumberInput.error = "Please enter your admin number"
                hasError = true
            }
            if (emailInput.text.isEmpty()) {
                emailInput.error = "Please enter your email address"
                hasError = true
            }
            if (pemGroupInput.text.isEmpty()) {
                pemGroupInput.error = "Please enter your pem group"
                hasError = true
            }
        }
        return !hasError
    }
}