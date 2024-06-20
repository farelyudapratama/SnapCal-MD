package com.application.snapcal.view.homeFragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.application.snapcal.R
import com.application.snapcal.databinding.FragmentHomeBinding
import com.application.snapcal.view.main.CalculatorActivity
import java.util.UUID

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var _binding: FragmentHomeBinding
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var viewPager2: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        viewPager2 = _binding.viewPager2
        val slideDot = _binding.slideDotLL

        val imageList = arrayListOf(
            ImageItem(UUID.randomUUID().toString(), "https://media.licdn.com/dms/image/C4D1BAQHOXJEkdbA76A/company-background_10000/0/1583503235651/foto_com_cover?e=2147483647&v=beta&t=IGkoc-V8F9__eIu3QizkYGvtS8Jfc8PdzxykYmqghW0"),
            ImageItem(UUID.randomUUID().toString(), "android.resource://" + requireContext().packageName + "/" + R.drawable.brokoli_rebus),
            ImageItem(UUID.randomUUID().toString(), "android.resource://" + requireContext().packageName + "/" + R.drawable.sup_ayam)
        )

        imageAdapter = ImageAdapter()
        viewPager2.adapter = imageAdapter
        imageAdapter.submitList(imageList)

        startAutoSlide()

        setupAction()
    }

    private fun startAutoSlide() {
        val runnable = object : Runnable {
            override fun run() {
                if (currentPage == imageAdapter.itemCount) {
                    currentPage = 0
                }
                viewPager2.setCurrentItem(currentPage++, true)
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