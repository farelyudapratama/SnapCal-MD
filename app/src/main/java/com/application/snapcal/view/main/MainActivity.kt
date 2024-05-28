package com.application.snapcal.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
        setupBottomNavigation()

    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.profile -> {
                    Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        binding.fabCameraX.setOnClickListener {
            Toast.makeText(this, "CameraX", Toast.LENGTH_SHORT).show()
        }
    }
    private fun setupAction() {
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            startActivity(Intent(this, ActLogin::class.java))
            finish()
        }
    }
}