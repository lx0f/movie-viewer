package nyp.sit.movieviewer.intermediate.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import nyp.sit.movieviewer.intermediate.databinding.ActivityVerifyCodeBinding
import nyp.sit.movieviewer.intermediate.ui.viewmodel.VerifyCodeViewModel

class VerifyCodeActivity : AppCompatActivity() {

    private val viewModel: VerifyCodeViewModel by viewModels { VerifyCodeViewModel.Factory }
    private lateinit var binding: ActivityVerifyCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyCodeBinding.inflate(layoutInflater)
        binding.submitButton.setOnClickListener {
            viewModel.viewModelScope.launch(Dispatchers.IO) {
                val code = binding.codeInput.text.toString().toInt()
                val verifyJob = async { viewModel.verify(code) }
                // TODO: Insert loading animation
                val result = verifyJob.await()
                if (result) {
                    val intent = Intent(this@VerifyCodeActivity, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    runOnUiThread {
                        binding.codeInput.error = "Wrong code."
                    }
                }
            }
        }
        setContentView(binding.root)
    }
}