package com.example.falaai.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.falaai.R
import com.example.falaai.constants.Constants
import com.example.falaai.databinding.FragmentHistoricChatBinding
import com.example.falaai.storage.ChatStorage
import com.example.falaai.ui.activity.ChatActivity
import com.example.falaai.ui.adapter.AdapterHistoricChat

class HistoricChatFragment : Fragment() {

    private var _binding: FragmentHistoricChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterHistoric: AdapterHistoricChat
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHistoricChatBinding.inflate(inflater, container, false)
        val view = binding.root

        adapterHistoric = AdapterHistoricChat(requireContext(), ChatStorage(requireContext()).getList())
        recyclerView = binding.homeRecyclerChat
        recyclerView.adapter = adapterHistoric

        adapterHistoric.setOnClickChat { modelChat ->
            startActivity(Intent(requireActivity(), ChatActivity::class.java).apply {
                this.putExtra(Constants.KEY_OPEN_CHAT, modelChat)
            })
        }

        binding.homeElevatedButton.setOnClickListener {
            startActivity(Intent(requireActivity(), ChatActivity::class.java))
        }

        return view
    }
}