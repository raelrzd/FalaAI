package com.example.falaai.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.falaai.databinding.FragmentSettingsUserBinding

class SettingsUserFragment : Fragment() {

    private var _binding: FragmentSettingsUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSettingsUserBinding.inflate(inflater, container, false)
        val view = binding.root



        return view
    }
}