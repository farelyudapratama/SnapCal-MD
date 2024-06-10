package com.application.snapcal.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.application.snapcal.R
import com.application.snapcal.databinding.FragmentHomeBinding
import com.application.snapcal.view.main.CalculatorActivity

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var _binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        setupAction()
    }

    private fun setupAction() {
        _binding.calculatorCard.setOnClickListener {
            startActivity(Intent(requireContext(), CalculatorActivity::class.java))
        }
    }
}