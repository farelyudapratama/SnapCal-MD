package com.application.snapcal.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.application.snapcal.databinding.ActivitySplashBinding
import com.application.snapcal.view.ViewModelFactory
import com.application.snapcal.view.login.ActLogin
import com.application.snapcal.view.main.MainActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewModel by viewModels<SplashViewModel>{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.getSession().observe(this) { user ->
                if (!user.isLogin) {
                    Log.d("token", "onCreate: ${user.token}")
                    startActivity(Intent(this, ActLogin::class.java))
                    finish()
                } else {
                    Log.d("token", "onCreate: ${user.token}")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }, 1000)
    }
}