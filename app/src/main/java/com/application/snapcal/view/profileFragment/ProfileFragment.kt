package com.application.snapcal.view.profileFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.application.snapcal.R
import com.application.snapcal.databinding.FragmentProfileBinding
import com.application.snapcal.view.EditProfileActivity
import com.application.snapcal.view.login.ActLogin

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private lateinit var _binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        setupAction()
    }

    private fun setupAction() {
        _binding.tvEditAkun.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }
        _binding.tvGantiKataSandi.setOnClickListener {

        }

        _binding.tvHapusAkun.setOnClickListener {

        }

        _binding.tvTentangAplikasi.setOnClickListener {

        }

        _binding.btnKeluar.setOnClickListener {
            viewModel.logout()
            startActivity(Intent(requireContext(), ActLogin::class.java))
        }
    }
}