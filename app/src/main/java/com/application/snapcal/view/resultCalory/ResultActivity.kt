package com.application.snapcal.view.resultCalory

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.application.snapcal.R
import com.application.snapcal.databinding.ActivityResultBinding
import com.bumptech.glide.Glide

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
        val label = intent.getStringExtra(EXTRA_LABEL)
        val caloriesString  = intent.getStringExtra(EXTRA_CALORIE)

        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            Glide.with(this)
                .load(imageUri)
                .into(binding.resultImage)
        } else {
            Toast.makeText(this, "Gambar gagal dimuat", Toast.LENGTH_SHORT).show()
        }

        binding.jumlahMakanan.text = label
        binding.jumlahKalori.text = caloriesString
    }

    companion object {
        const val EXTRA_IMAGE_URI = "image_uri"
        const val EXTRA_LABEL = "label"
        const val EXTRA_CALORIE = "calorie"
    }
}
