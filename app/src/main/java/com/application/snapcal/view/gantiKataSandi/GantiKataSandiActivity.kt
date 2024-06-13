package com.application.snapcal.view.gantiKataSandi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.application.snapcal.data.ResultState
import com.application.snapcal.databinding.ActivityGantiKataSandiBinding
import com.application.snapcal.view.ViewModelFactory

class GantiKataSandiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGantiKataSandiBinding
    private val viewModel by viewModels<GantiKataSandiViewModel>(){
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGantiKataSandiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSimpan.setOnClickListener {
            val newPassword = binding.newPassword.text.toString()
            val confirmPassword = binding.newPasswordConfirm.text.toString()

            if (newPassword.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (newPassword == confirmPassword) {
                    dialog(newPassword)
                } else {
                    Toast.makeText(this, "Password baru tidak cocok", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBatal.setOnClickListener {
            finish()
        }

        viewModel.resetPasswordResult.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    // Tampilkan loading indicator
                }
                is ResultState.Success -> {
                    Toast.makeText(this, "Kata sandi berhasil diubah", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is ResultState.Error -> {
                    Toast.makeText(this, result.error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun dialog(newPassword: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Konfirmasi")
            setMessage("Apakah Anda yakin ingin mengubah kata sandi?")
            setPositiveButton("Ya") { _, _ ->
                viewModel.resetPassword(newPassword)
            }
            setNegativeButton("Tidak", null)
            create()
            show()
        }
    }
}