package com.example.demos.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.example.demos.models.IntroSlide
import com.example.demos.ui.adapters.IntroSliderAdapter
import com.example.demos.R
import com.example.demos.databinding.ActivityIntroSliderBinding
import com.example.demos.models.Intro
import com.example.demos.utils.Constants

private val intros = arrayOfNulls<Intro>(Constants.MAX_STEP)

class IntroActivity : AppCompatActivity() {
    lateinit var binding: ActivityIntroSliderBinding

    private val introSliderAdapter = IntroSliderAdapter(
        listOf(
            IntroSlide(
                "Membaca",
                "Berita realtime, update instansi pemerintahan, terpercaya, tetap terupdate, berbagai sumber, pengguna aplikasi.",
                R.drawable.reading
            ),
            IntroSlide(
                "Memahami",
                "Membantu pengguna memahami kebijakan pemerintah dengan menyediakan sumber informasi, analisis, dan materi pembelajaran yang mudah dipahami.",
                R.drawable.certificate
            ),
            IntroSlide(
                "Memberikan Aspirasi",
                "Berpartisipasi aktif dalam opini kebijakan pemerintah melalui platform khusus: ungkapkan sudut pandang, berbagi ide, pertanyaan, pengaruh proses demokrasi.",
                R.drawable.talk
            )
        )
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroSliderBinding.inflate(layoutInflater)
        binding.introSliderViewPager.adapter = introSliderAdapter
        setContentView(binding.root)
        setupIndicators()
        setCurrentIndicator(0)
        binding.introSliderViewPager.registerOnPageChangeCallback(object :
        ViewPager2.OnPageChangeCallback(){

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
        binding.buttonNext.setOnClickListener {
            if (binding.introSliderViewPager.currentItem + 1 < introSliderAdapter.itemCount) {
                binding.introSliderViewPager.currentItem += 1
            } else {
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        binding.textSkipIntro.setOnClickListener{
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(introSliderAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(8,0,8,0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext, R.drawable.indicator_inactvie
                    )
                )
                this?.layoutParams = layoutParams
            }
            binding.indicatorsContainer.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = binding.indicatorsContainer.childCount
        for(i in 0 until childCount) {
            val imageView = binding.indicatorsContainer[i] as ImageView
            if(i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active
                    )
                )
            }else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactvie
                    )
                )
            }
        }
    }
}