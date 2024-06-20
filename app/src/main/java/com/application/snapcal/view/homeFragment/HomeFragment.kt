package com.application.snapcal.view.homeFragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.application.snapcal.R
import com.application.snapcal.databinding.FragmentHomeBinding
import com.application.snapcal.view.main.CalculatorActivity
import java.util.UUID
import kotlin.math.abs

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var _binding: FragmentHomeBinding
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage1 = 0
    private var currentPage2 = 0
    private lateinit var imageAdapter1: ImageAdapterPromote
    private lateinit var imageAdapter2: ImageAdapterRecipe
    private lateinit var viewPager1: ViewPager2
    private lateinit var viewPager2: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        viewPager1 = _binding.viewPager1
        viewPager2 = _binding.viewPager2

        val imagePromote = arrayListOf(
            ImageItem(UUID.randomUUID().toString(), "android.resource://" + requireContext().packageName + "/" + R.drawable.promote1),
            ImageItem(UUID.randomUUID().toString(), "android.resource://" + requireContext().packageName + "/" + R.drawable.promote2),
            ImageItem(UUID.randomUUID().toString(), "android.resource://" + requireContext().packageName + "/" + R.drawable.promote3)
        )

        val imageRecipe = arrayListOf(
            ImageItem(UUID.randomUUID().toString(), "android.resource://" + requireContext().packageName + "/" + R.drawable.recipe1),
            ImageItem(UUID.randomUUID().toString(), "android.resource://" + requireContext().packageName + "/" + R.drawable.recipe2)
        )

        imageAdapter1 = ImageAdapterPromote()
        imageAdapter2 = ImageAdapterRecipe()

        viewPager1.adapter = imageAdapter1
        viewPager2.adapter = imageAdapter2

        imageAdapter1.submitList(imagePromote)
        imageAdapter2.submitList(imageRecipe)

        startAutoSlide()
        setUpTransformer()
        setupAction()
    }

    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }
        viewPager1.setPageTransformer(transformer)
        viewPager2.setPageTransformer(transformer)
    }

    private fun startAutoSlide() {
        val runnable = object : Runnable {
            override fun run() {
                if (currentPage1 == imageAdapter1.itemCount) {
                    currentPage1 = 0
                }
                if (currentPage2 == imageAdapter2.itemCount) {
                    currentPage2 = 0
                }
                viewPager1.setCurrentItem(currentPage1++, true)
                viewPager2.setCurrentItem(currentPage2++, true)
                handler.postDelayed(this, 3000)
            }
        }
        handler.post(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    private fun setupAction() {
        _binding.calculatorCard.setOnClickListener {
            startActivity(Intent(requireContext(), CalculatorActivity::class.java))
        }
    }
}