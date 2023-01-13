package nyp.sit.movieviewer.basic.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import nyp.sit.movieviewer.basic.databinding.ActivityRegisterBinding
import nyp.sit.movieviewer.basic.domain.RegisterStatus
import nyp.sit.movieviewer.basic.entity.User
import nyp.sit.movieviewer.basic.ui.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private val TAG: String? = this::class.simpleName
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels { RegisterViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        binding.registerButton.setOnClickListener {
            submit()
        }
        setContentView(binding.root)
    }

    private fun submit() {
        val statusObserver = Observer<RegisterStatus> { status: RegisterStatus? ->
            when (status) {
                RegisterStatus.FAIL -> Log.d(TAG, "FAIL")
                RegisterStatus.LOADING, null -> Log.d(TAG, "LOADING...")
                RegisterStatus.SUCCESS -> {
                    Log.d(TAG, "SUCCESS")
                    val intent = Intent(this, VerifyCodeActivity::class.java)
                    startActivity(intent)
                }
                RegisterStatus.LOGIN_NAME_EXITS -> {
                    binding.loginNameInput.error = "Enter a different login name."
                }
                RegisterStatus.ADMIN_NUMBER_EXISTS -> {
                    binding.adminNumberInput.error = "Enter a different admin number."
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
}