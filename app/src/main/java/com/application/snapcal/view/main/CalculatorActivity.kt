package com.application.snapcal.view.main

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.application.snapcal.R

class CalculatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        // Initialize views
        val etTinggiBadan = findViewById<EditText>(R.id.et_tinggi_badan)
        val etBeratBadan = findViewById<EditText>(R.id.et_berat_badan)
        val btnHitungBmi = findViewById<Button>(R.id.btn_hitung_bmi)
        val tvHasilBmi = findViewById<TextView>(R.id.tv_hasil_bmi)

        // Set click listener for the button
        btnHitungBmi.setOnClickListener {
            val tinggiBadanStr = etTinggiBadan.text.toString()
            val beratBadanStr = etBeratBadan.text.toString()

            // Validate input
            if (tinggiBadanStr.isEmpty() || beratBadanStr.isEmpty()) {
                Toast.makeText(this, "Mohon masukkan tinggi dan berat badan", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Convert input to numbers
            val tinggiBadan = tinggiBadanStr.toFloatOrNull()
            val beratBadan = beratBadanStr.toFloatOrNull()

            if (tinggiBadan == null || beratBadan == null) {
                Toast.makeText(this, "Masukkan nilai yang valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Calculate BMI
            val tinggiBadanMeter = tinggiBadan / 100 // convert cm to meters
            val bmi = beratBadan / (tinggiBadanMeter * tinggiBadanMeter)

            // Determine BMI category
            val kategori = getBMICategory(bmi)

            // Display the result
            tvHasilBmi.text = String.format("BMI: %.2f (%s)", bmi, kategori)
        }
    }

    private fun getBMICategory(bmi: Float): String {
        return when {
            bmi < 18.5 -> "Kekurangan berat badan"
            bmi in 18.5..24.9 -> "Berat badan normal"
            bmi in 25.0..29.9 -> "Kelebihan berat badan"
            bmi >= 30.0 -> "Kegemukan"
            else -> "Tidak diketahui"
        }
    }
}