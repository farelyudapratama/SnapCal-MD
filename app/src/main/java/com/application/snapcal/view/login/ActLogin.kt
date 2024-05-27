package com.application.snapcal.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.application.snapcal.data.api.ApiService
import com.application.snapcal.data.pref.UserModel
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
            viewModel.saveSession(UserModel(email, "sample_token"))
            AlertDialog.Builder(this).apply {
                setTitle("Yeah!")
                setMessage("Anda berhasil login. Sudah tidak sabar untuk belajar ya?")
                setPositiveButton("Lanjut") { _, _ ->
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        }
        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this, ActSignUp::class.java)
            startActivity(intent)
        }
//        val email = binding.edEmailLogin.getText().toString()
//        val password = binding.edPassLogin.getText().toString()
//        val loginRequest = LoginRequest(email, password)
//        val call: Call<LoginResponse?>? = apiService!!.login(loginRequest)
//        call.enqueue(object : Callback<LoginResponse?>() {
//            fun onResponse(call: Call<LoginResponse?>?, response: Response<LoginResponse?>) {
//                if (response.isSuccessful() && response.body() != null) {
//                    Toast.makeText(
//                        this@LoginActivity,
//                        response.body().getMessage(),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    Toast.makeText(this@LoginActivity, "Masuk Aplikasi Gagal!", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            fun onFailure(call: Call<LoginResponse?>?, t: Throwable) {
//                Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
//            }
//        })
    }
}