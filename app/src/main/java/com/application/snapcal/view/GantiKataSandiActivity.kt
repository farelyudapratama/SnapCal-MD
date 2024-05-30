package com.application.snapcal.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.application.snapcal.R
import com.application.snapcal.databinding.ActivityGantiKataSandiBinding

class GantiKataSandiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGantiKataSandiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGantiKataSandiBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}