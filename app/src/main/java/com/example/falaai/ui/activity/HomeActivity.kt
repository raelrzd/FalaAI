package com.example.falaai.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.falaai.R
import com.example.falaai.databinding.ActivityHomeBinding
import com.google.android.material.navigation.NavigationBarView

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        NavigationBarView.OnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.item_1 -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.item_2 -> {
                    // Respond to navigation item 2 click
                    true
                }
                else -> false
            }
        }

    }
}