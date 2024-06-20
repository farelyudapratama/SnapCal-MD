package com.application.snapcal.view.cameraX

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.application.snapcal.R
import com.application.snapcal.databinding.ActivityCameraXactivityBinding
import com.application.snapcal.view.resultCalory.ResultActivity
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.InputStream
import java.text.NumberFormat
import java.util.Locale

class CameraXActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraXactivityBinding
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    private var isFlashOn = false
    private var uri: Uri? = null
    private lateinit var cameraControl: CameraControl
    private lateinit var imageClassifier: ImageClassifier

    private var result:String? = null
    private var prediction:String? = null
    private var score:String? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
        if (imageUri != null) {
            uri = imageUri
            val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            analyzeImage(bitmap, uri!!)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this,
                    getString(R.string.permission_request_granted), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
                finish()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraXactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        } else {
            startCamera()
        }
        binding.switchCamera.setOnClickListener {
            cameraSelector =
                if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }
        binding.captureImage.setOnClickListener { takePhoto() }
        binding.openGallery.setOnClickListener { startGallery() }
        binding.flashLight.setOnClickListener { toggleFlash() }
    }

    // CAMERA
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                val camera = cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
                cameraControl = camera.cameraControl

            } catch (exc: Exception) {
                Toast.makeText(
                    this@CameraXActivity,
                    getString(R.string.gagal_memunculkan_kamera),
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = createCustomTempFile(application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                    uri = savedUri
                    val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                    analyzeImage(bitmap, uri!!)
                }

                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraXActivity,
                        getString(R.string.gagal_mengambil_gambar),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "onError: ${exc.message}")
                }
            }
        )
    }

    // GALLERY
    private fun startGallery() {
        pickImage.launch("image/*")
    }

    // FLASH
    private fun toggleFlash() {
        isFlashOn = !isFlashOn
        cameraControl.enableTorch(isFlashOn)

        if (isFlashOn) {
            binding.flashLight.setImageResource(R.drawable.baseline_flashlight_on_24)
        } else {
            binding.flashLight.setImageResource(R.drawable.baseline_flashlight_off_24)
        }
    }

    // Analyze Image
    private fun analyzeImage(bitmap: Bitmap?, imageUri: Uri) {
        bitmap?.let {
            imageClassifier = ImageClassifier(
                context = this,
                classifierListener = object : ImageClassifier.ClassifierListener {
                    override fun onError(error: String) {
                        showToast(error)
                    }

                    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                        results?.let { it ->
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                val sortedCategories =
                                    it[0].categories.sortedByDescending { it?.score }
                                result =
                                    sortedCategories.joinToString("\n") {
                                        "${it.label} " + NumberFormat.getPercentInstance()
                                            .format(it.score).trim()
                                    }
                                prediction = sortedCategories[0].label
                                score = NumberFormat.getPercentInstance().format(sortedCategories[0].score)

                                val calories = foodCaloriesMap[prediction?.lowercase(Locale.getDefault())]

                                val intent = Intent(this@CameraXActivity, ResultActivity::class.java)
                                intent.putExtra(ResultActivity.EXTRA_LABEL, result)
                                intent.putExtra(ResultActivity.EXTRA_CALORIE, calories?.toString())
                                intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, imageUri.toString())

                                startActivity(intent)
                            } else {
                                showToast("No result found")
                            }
                        }
                    }
                }
            )
            bitmap.let { this.imageClassifier.classifyStaticImage(it) }

//            navigateToResultActivity(imageUri, label, caloriesString)
        }
    }

//    private fun buildConfidenceString(confidenceScores: Map<String, Any>): String {
//        confidenceScores.forEach { (_, value) ->
//            if (value is Int) {
//                return "$value Kalori"
//            }
//        }
//        return "Tidak valid"
//    }
//    // NAVIGATE TO RESULT ACTIVITY
//    private fun navigateToResultActivity(imageUri: Uri, label: String, caloriesString: String) {
//        val intent = Intent(this, ResultActivity::class.java).apply {
////            putExtra(ResultActivity.EXTRA_IMAGE_URI, imageUri.toString())
////            putExtra(ResultActivity.EXTRA_LABEL, label)
////            putExtra(ResultActivity.EXTRA_CALORIE, caloriesString)
//        }
//        startActivity(intent)
//    }
    private fun showToast(message: String = "No result found") {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    companion object {
        private const val REQUIRED_PERMISSION = android.Manifest.permission.CAMERA
        private const val TAG = "CameraActivity"
    }
}