package com.example.falaai.ui.activity

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Build
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.example.falaai.R
import com.example.falaai.databinding.ActivityHomeBinding
import com.example.falaai.ui.fragment.HistoricChatFragment
import com.example.falaai.ui.fragment.SettingsUserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navView: BottomNavigationView
    private val historicChatFragment = HistoricChatFragment()
    private val settingsUserFragment = SettingsUserFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupAnimations()
        setupNavigationBar()
    }

    override fun onResume() {
        super.onResume()
        historicChatFragment.updateAdapterList()
    }

    private fun setupAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                    splashScreenView,
                    PropertyValuesHolder.ofFloat("scaleX", 1.7f),
                    PropertyValuesHolder.ofFloat("scaleY", 1.7f)
                )

                scaleDown.duration = 500
                scaleDown.repeatCount = 3
                scaleDown.repeatMode = ObjectAnimator.REVERSE
                scaleDown.interpolator = AccelerateDecelerateInterpolator()

                scaleDown.doOnEnd { splashScreenView.remove() }

                scaleDown.start()
            }
        }
    }

    private fun setupNavigationBar() {
        navView = binding.homeNavigationBar

        supportFragmentManager.beginTransaction().replace(R.id.homeContainer, historicChatFragment)
            .commit()

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.historicChatFragment -> {
                    openFragment(historicChatFragment)
                    true
                }

                R.id.settingsUserFragment -> {
                    openFragment(settingsUserFragment)
                    true
                }

                else -> false
            }

        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.homeContainer, fragment).commit()
    }

}