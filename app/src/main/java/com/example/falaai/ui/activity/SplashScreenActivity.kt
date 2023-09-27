package com.example.falaai.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.falaai.R
import com.example.falaai.databinding.ActivitySplashScreenBinding

private const val COLOR_STATUS_BAR = "#94cd9a"

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        window.statusBarColor = Color.parseColor(COLOR_STATUS_BAR)
        setupAnimationImageView()
        nextActivity()
    }

    private fun nextActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 4000)
    }

    private fun setupAnimationImageView() {
        val pulse: Animation = AnimationUtils.loadAnimation(this, R.anim.pulse)
        binding.splashScreenImage.animation = pulse
    }

}