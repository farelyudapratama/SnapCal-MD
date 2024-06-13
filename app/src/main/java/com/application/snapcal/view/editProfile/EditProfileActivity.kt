package com.application.snapcal.view.editProfile

import android.R
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.application.snapcal.data.ResultState
import com.application.snapcal.databinding.ActivityEditProfilBinding
import com.application.snapcal.view.CustomSpinnerAdapter
import com.application.snapcal.view.ViewModelFactory
import com.application.snapcal.view.cameraX.createCustomTempFile
import com.application.snapcal.view.cameraX.reduceFileImage
import com.application.snapcal.view.cameraX.uriToFile
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.concurrent.Executors

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditProfilBinding
    private val viewModel by viewModels<EditProfileViewModel>(){
        ViewModelFactory.getInstance(this)
    }
    private val genderMap = mapOf(
        "Laki-laki" to "male",
        "Perempuan" to "female"
    )
    private var uri: Uri? = null

    private var currentName: String? = null
    private var currentEmail: String? = null
    private var currentGender: String? = null
    private var currentWeight: Int? = null
    private var currentHeight: Int? = null
    private var currentAge: Int? = null
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        val gender = resources.getStringArray(R.array.gender_array)
        val genderList = genderMap.keys.toList()

        val adapter = CustomSpinnerAdapter(this, R.layout.simple_spinner_item, genderList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGender.adapter = adapter

        viewModel.profileResult.observe(this){ result ->
            when (result) {
                is ResultState.Loading -> {
                    // Tampilkan loading indicator
                }
                is ResultState.Success -> {
                    binding.apply {
                        etUsername.setText(result.data.data?.name)
                        etEmail.setText(result.data.data?.email)
                        etWeight.setText(result.data.data?.beratBadan?.toString())
                        etHeight.setText(result.data.data?.tinggiBadan?.toString())
                        etAge.setText(result.data.data?.usiaUser?.toString())

                        result.data.data?.gambarProfil?.let { imageUrl ->
                            currentImageUri = Uri.parse(imageUrl)
                            Glide.with(this@EditProfileActivity)
                                .load(currentImageUri)
                                .into(ivAvatar)
                        }
                    }
                    val genderResponse = genderMap.entries.find { it.value == result.data.data?.gender}?.key
                    val genderPosition = genderList.indexOf(genderResponse)
                    if (genderPosition >= 0) {
                        binding.spinnerGender.setSelection(genderPosition)
                    }

                    binding.spinnerGender.setSelection(genderPosition)

                    currentName = result.data.data?.name
                    currentEmail = result.data.data?.email
                    currentGender = result.data.data?.gender
                    currentWeight = result.data.data?.beratBadan
                    currentHeight = result.data.data?.tinggiBadan
                    currentAge = result.data.data?.usiaUser
                }
                is ResultState.Error -> {
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.btnSimpan.setOnClickListener { saveProfileEdit() }

        binding.btnBatal.setOnClickListener {
            finish()
        }

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
            if (imageUri != null) {
                uri = imageUri
                Glide.with(this)
                    .load(uri)
                    .into(binding.ivAvatar)
            }
        }

        binding.fabEdit.setOnClickListener {
            pickImage.launch("image/*")
        }
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
//        val finalImage = if (uri != null) uriToFile(uri!!, this).reduceFileImage() else currentImageUri

        if (uri != null) {
            currentImageUri?.let { uri ->
                if (uri.scheme == "http" || uri.scheme == "https") {
                    downloadImageFromUrl(uri.toString()) { file ->
                        file?.let {
                            val reducedFile = it.reduceFileImage()
                            viewModel.uploadProfilePhoto(reducedFile)
                        } ?: run {
                            Log.e("EditProfileActivity", "Error downloading image from URL")
                            Toast.makeText(this, "Failed to download image", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    try {
                        val file = uriToFile(uri, this)
                        val reducedFile = file.reduceFileImage()
                        viewModel.uploadProfilePhoto(reducedFile)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e("EditProfileActivity", "Error uploading profile photo: ${e.message}")
                        Toast.makeText(this, "Failed to process image", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            viewModel.saveProfileChanges(
                finalName.toString(),
                finalEmail.toString(),
                finalGender,
                finalWeight,
                finalHeight,
                finalAge
            )
        } else {
            viewModel.saveProfileChanges(
                finalName.toString(),
                finalEmail.toString(),
                finalGender,
                finalWeight,
                finalHeight,
                finalAge
            )
        }
    }
    private fun downloadImageFromUrl(url: String, callback: (File?) -> Unit) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            try {
                val inputStream = URL(url).openStream()
                val tempFile = createCustomTempFile(this)
                val outputStream = FileOutputStream(tempFile)
                inputStream.copyTo(outputStream)
                inputStream.close()
                outputStream.close()
                handler.post {
                    callback(tempFile)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                handler.post {
                    callback(null)
                }
            }
        }
    }

}