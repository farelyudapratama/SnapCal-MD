package com.application.snapcal.view.profileFragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.application.snapcal.R
import com.application.snapcal.data.ResultState
import com.application.snapcal.databinding.FragmentProfileBinding
import com.application.snapcal.view.GantiKataSandiActivity
import com.application.snapcal.view.ViewModelFactory
import com.application.snapcal.view.editProfile.EditProfileActivity
import com.application.snapcal.view.login.ActLogin
import com.bumptech.glide.Glide


class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private lateinit var _binding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileViewModel>{
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        viewModel.profileItem.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> {
                    // Tampilkan loading indicator
                }
                is ResultState.Success -> {
                    _binding.apply {
                        Glide
                            .with(requireContext())
                            .load(result.data.data?.gambarProfil)
                            .into(ivAvatar)
                        tvName.text = result.data.data?.name
                        tvEmail.text = result.data.data?.email
                    }
                }
                is ResultState.Error -> {
                    // Tampilkan pesan error
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
        setupAction()
    }

    private fun setupAction() {
        _binding.tvEditAkun.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }
        _binding.tvGantiKataSandi.setOnClickListener {
            startActivity(Intent(requireContext(), GantiKataSandiActivity::class.java))
        }

        _binding.tvHapusAkun.setOnClickListener {

        }

        _binding.tvTentangAplikasi.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.layout_dialog_info_app)

            dialog.show()
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            dialog.window!!.setGravity(Gravity.BOTTOM)
        }

        _binding.btnKeluar.setOnClickListener {
            viewModel.logout()
            startActivity(Intent(requireContext(), ActLogin::class.java))
        }
    }
}