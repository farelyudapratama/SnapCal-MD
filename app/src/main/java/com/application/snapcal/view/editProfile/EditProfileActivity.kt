package com.application.snapcal.view.editProfile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.application.snapcal.data.ResultState
import com.application.snapcal.databinding.ActivityEditProfilBinding
import com.application.snapcal.view.CustomSpinnerAdapter
import com.application.snapcal.view.ViewModelFactory
import com.application.snapcal.view.cameraX.reduceFileImage
import com.application.snapcal.view.cameraX.uriToFile
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfilBinding
    private val viewModel by viewModels<EditProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val genderMap = mapOf(
        "Laki-laki" to "male",
        "Perempuan" to "female"
    )
    private val PICK_IMAGE_REQUEST = 1
    private val REQUEST_PERMISSION = 2
    private fun checkAndRequestPermissions(): Boolean {
        val readExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val writeExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val listPermissionsNeeded = mutableListOf<String>()

        if (readExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(), REQUEST_PERMISSION)
            return false
        }

        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Izin diberikan, lanjutkan proses
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, PICK_IMAGE_REQUEST)
                } else {
                    // Izin ditolak, beri informasi kepada pengguna
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private var currentName: String? = null
    private var currentEmail: String? = null
    private var currentGender: String? = null
    private var currentWeight: Int? = null
    private var currentHeight: Int? = null
    private var currentAge: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val genderList = genderMap.keys.toList()

        val adapter = CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, genderList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGender.adapter = adapter

        viewModel.profileResult.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    // Tampilkan loading indicator
                    showLoading(true)
                }
                is ResultState.Success -> {
                    binding.apply {
                        etUsername.setText(result.data.data?.name)
                        etEmail.setText(result.data.data?.email)
                        etWeight.setText(result.data.data?.beratBadan?.toString())
                        etHeight.setText(result.data.data?.tinggiBadan?.toString())
                        etAge.setText(result.data.data?.usiaUser?.toString())

                        result.data.data?.gambarProfil?.let { imageUrl ->
                            Glide.with(this@EditProfileActivity)
                                .load(imageUrl)
                                .into(ivAvatar)
                        }
                    }
                    val genderResponse = genderMap.entries.find { it.value == result.data.data?.gender }?.key
                    val genderPosition = genderList.indexOf(genderResponse)
                    if (genderPosition >= 0) {
                        binding.spinnerGender.setSelection(genderPosition)
                    }

                    currentName = result.data.data?.name
                    currentEmail = result.data.data?.email
                    currentGender = result.data.data?.gender
                    currentWeight = result.data.data?.beratBadan
                    currentHeight = result.data.data?.tinggiBadan
                    currentAge = result.data.data?.usiaUser
                    showLoading(false)
                }
                is ResultState.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.btnSimpan.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Simpan Perubahan")
                setMessage("Anda yakin ingin menyimpan perubahan?")
                setPositiveButton("Ya") { _, _ ->
                    saveProfileEdit()
                }
                setNegativeButton("Tidak", null)
                create()
                show()
            }
        }

        binding.btnBatal.setOnClickListener {
            finish()
        }

        binding.fabEdit.setOnClickListener {

            if (checkAndRequestPermissions()) {
                pickImage.launch("image/*")
            }
        }
    }
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imageUri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()

            AlertDialog.Builder(this).apply {
                setTitle("Upload Photo")
                setMessage("Anda yakin ingin mengupload foto ini?")
                setPositiveButton("Ya") { _, _ ->
                    uploadPhoto(imageFile)

                    Glide.with(this@EditProfileActivity)
                        .load(imageUri)
                        .apply(RequestOptions.circleCropTransform())
                        .into(binding.ivAvatar)
                }
                setNegativeButton("Tidak", null)
                create()
                show()
            }
        }
    }

    private fun uploadPhoto(filePath: File) {
        viewModel.uploadPhoto(filePath).observe(this@EditProfileActivity, Observer { response ->
            if (response != null) {
                Toast.makeText(this@EditProfileActivity, "Berhasil Upload Foto", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@EditProfileActivity, "Gagal Upload Foto", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveProfileEdit() {
        val name = binding.etUsername.text.toString()
        val email = binding.etEmail.text.toString()
        val gender = genderMap[binding.spinnerGender.selectedItem.toString()]
        val weight = binding.etWeight.text.toString().toIntOrNull()
        val height = binding.etHeight.text.toString().toIntOrNull()
        val age = binding.etAge.text.toString().toIntOrNull()

        val finalName = name.takeIf { it.isNotEmpty() } ?: currentName
        val finalEmail = email.takeIf { it.isNotEmpty() } ?: currentEmail
        val finalGender = gender ?: currentGender
        val finalWeight = weight ?: currentWeight
        val finalHeight = height ?: currentHeight
        val finalAge = age ?: currentAge

        viewModel.saveProfileChanges(
            finalName.toString(),
            finalEmail.toString(),
            finalGender,
            finalWeight,
            finalHeight,
            finalAge
        )
        finish()
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }
}
