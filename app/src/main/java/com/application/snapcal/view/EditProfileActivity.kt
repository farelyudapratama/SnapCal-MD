package com.application.snapcal.view

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.application.snapcal.R
import com.application.snapcal.databinding.ActivityEditProfilBinding

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditProfilBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val gender = resources.getStringArray(R.array.gender_array)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, gender)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGender.adapter = adapter
    }
}