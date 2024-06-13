package com.application.snapcal.view.signUp

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.application.snapcal.data.ResultState
import com.application.snapcal.databinding.ActivityActSignUpBinding
import com.application.snapcal.view.ViewModelFactory
import com.application.snapcal.view.login.ActLogin


class ActSignUp : AppCompatActivity() {
    private lateinit var binding: ActivityActSignUpBinding
    private val viewModel by viewModels<SignUpViewModel>(){
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        apiServ = APIclients.getClients().create(ApiService::class.java)
        binding.btnSignup.setOnClickListener { registerUser() }
    }

    private fun registerUser() {
        val name = binding.userSignup.text.toString()
        val email = binding.emailSignup.text.toString()
        val password = binding.passSignup.text.toString()

        viewModel.signUp(name, email, password)

        viewModel.signUpResult.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    // Tampilkan loading indicator
                }

                is ResultState.Success -> {
                    val successMessage = result.data.message
                    Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, ActLogin::class.java)
                    startActivity(intent)
                    finish()
                }

                is ResultState.Error -> {
                    val errorMessage = result.error
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}