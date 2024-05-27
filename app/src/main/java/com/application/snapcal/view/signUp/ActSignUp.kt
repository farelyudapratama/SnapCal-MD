package com.application.snapcal.view.signUp

import android.R
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.application.snapcal.databinding.ActivityActSignUpBinding


class ActSignUp : AppCompatActivity() {
    private lateinit var binding: ActivityActSignUpBinding
//    private var apiServ: APIserv? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        apiServ = APIclients.getClients().create(ApiService::class.java)
        binding.btnSignup.setOnClickListener { registerUser() }
    }

    private fun registerUser() {
//        val name = edtName!!.getText().toString()
//        val email = edtEmail!!.getText().toString()
//        val password = edtPassword!!.getText().toString()
//        val reqSignup = ReqSignup(name, email, password)
//        val call: Call<ResSignup?>? = apiServ!!.signup(ReqSignup)
//        call.enqueue(object : Callback<ResSignup?>() {
//            fun onResponse(call: Call<ResSignup?>?, response: Response<ResSignup?>) {
//                if (response.isSuccessful() && response.body() != null) {
//                    Toast.makeText(
//                        this@ActSignUp,
//                        response.body().getMessage(),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    Toast.makeText(this@ActSignUp, "Pendaftaran Gagal", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            fun onFailure(call: Call<ResSignup?>?, t: Throwable) {
//                Toast.makeText(this@ActSignUp, t.message, Toast.LENGTH_SHORT).show()
//            }
//        })
    }
}