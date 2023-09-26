package com.example.falaai.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.falaai.R
import com.example.falaai.databinding.FragmentHistoricChatBinding
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