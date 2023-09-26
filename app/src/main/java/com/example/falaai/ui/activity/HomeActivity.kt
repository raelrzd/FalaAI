package com.example.falaai.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupNavigationBar()


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