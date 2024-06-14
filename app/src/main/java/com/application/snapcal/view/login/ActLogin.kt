package com.application.snapcal.view.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.application.snapcal.data.ResultState
import com.application.snapcal.data.response.LoginResponse
import com.application.snapcal.databinding.ActivityActLoginBinding
import com.application.snapcal.view.ViewModelFactory
import com.application.snapcal.view.main.MainActivity
import com.application.snapcal.view.signUp.ActSignUp

class ActLogin : AppCompatActivity() {
    private lateinit var binding: ActivityActLoginBinding
    private val viewModel by viewModels<LoginViewModel>(){
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        apiService = ApiClient.getClient().create(ApiService::class.java)
        setupAction()
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edEmailLogin.text.toString()
            val password = binding.edPassLogin.text.toString()
            viewModel.login(email, password)
//            viewModel.saveSession(UserModel(email, "sample_token"))
            viewModel.loginResult.observe(this){ result ->
                when(result){
                    is ResultState.Loading -> {
                        // Tampilkan loading indicator
                        showLoading(true)
                    }
                    is ResultState.Success -> {
                        AlertDialog.Builder(this).apply {
                            val response : LoginResponse = result.data
                            setTitle("Yeah!")
                            setMessage("Anda berhasil login? ${response.message}")
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                            create()
                            show()
                        }
                        showLoading(false)
                    }
                    is ResultState.Error -> {
                        showLoading(false)
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this, ActSignUp::class.java)
            startActivity(intent)
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }
}