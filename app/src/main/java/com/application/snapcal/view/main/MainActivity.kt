package com.application.snapcal.view.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.application.snapcal.R
import com.application.snapcal.databinding.ActivityMainBinding
import com.application.snapcal.view.ViewModelFactory
import com.application.snapcal.view.login.ActLogin

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>{
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            startActivity(Intent(this, ActLogin::class.java))
            finish()
        }
    }
}