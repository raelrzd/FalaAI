package com.example.falaai.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.falaai.R
import com.example.falaai.constant.Constants
import com.example.falaai.constant.EnumAction
import com.example.falaai.constant.EnumAlertType
import com.example.falaai.databinding.FragmentHistoricChatBinding
import com.example.falaai.extension.showMessage
import com.example.falaai.model.ModelAction
import com.example.falaai.model.ModelChat
import com.example.falaai.storage.ChatStorage
import com.example.falaai.storage.UserStorage
import com.example.falaai.ui.activity.ChatActivity
import com.example.falaai.ui.adapter.AdapterHistoricChat
import com.example.falaai.ui.dialog.DialogValidation

class HistoricChatFragment : Fragment() {

    private var _binding: FragmentHistoricChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterHistoric: AdapterHistoricChat
    private lateinit var historicChatList: MutableList<ModelChat>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHistoricChatBinding.inflate(inflater, container, false)
        val view = binding.root
        setupRecyclerView()
        setupButtonNewChat()

        return view
    }

    override fun onResume() {
        super.onResume()
        checkUser()
        updateAdapterList()
    }

    private fun checkUser() {
        val userName = UserStorage(requireContext()).getUser()?.name
        binding.homeLabelWelcome.text =
            if (!userName.isNullOrBlank()) "Olá, $1! Como posso ajudar hoje?".replace(
                "$1",
                userName
            ) else "Seja bem vindo! Como posso te ajudar?"
    }

    private fun setupRecyclerView() {
        historicChatList = ChatStorage(requireContext()).getList()
        adapterHistoric = AdapterHistoricChat(requireContext(), historicChatList)
        recyclerView = binding.homeRecyclerChat
        recyclerView.adapter = adapterHistoric

        adapterHistoric.apply {
            setOnClickChat { modelChat ->
                startActivity(Intent(requireActivity(), ChatActivity::class.java).apply {
                    this.putExtra(Constants.KEY_OPEN_CHAT, modelChat)
                })
            }
            setOnLongClickChat { modelChat ->
                val action = ModelAction(
                    action = EnumAction.DELETE_CHAT,
                    icon = R.drawable.ic_delete_chats,
                    title = "Apagar conversa",
                    description = "Tem certeza que deseja remover esta conversa?"
                )
                DialogValidation(requireContext(), action).apply {
                    setOnClickConfirm {
                        removeItemChat(modelChat)
                    }
                    show()
                }

            }
        }

    }

    private fun removeItemChat(modelChat: ModelChat) {
        ChatStorage(requireContext()).removeItem(modelChat)
        val index = historicChatList.indexOf(modelChat)
        historicChatList.remove(modelChat)
        adapterHistoric.notifyItemRemoved(index)
        checkEmptyList()
        val appCompatActivity = requireActivity() as AppCompatActivity
        appCompatActivity.showMessage("Conversa removida com sucesso!", EnumAlertType.SUCCESS)
    }

    private fun updateAdapterList() {
        historicChatList.clear()
        historicChatList.addAll(ChatStorage(requireContext()).getList())
        adapterHistoric.notifyDataSetChanged()
        checkEmptyList()
    }

    private fun setupButtonNewChat() {
        binding.homeElevatedButton.setOnClickListener {
            startActivity(Intent(requireActivity(), ChatActivity::class.java))
        }
    }

    private fun checkEmptyList() {
        if (adapterHistoric.itemCount == 0) {
            binding.homeLayoutEmptyList.visibility = View.VISIBLE
            binding.homeRecyclerChat.visibility = View.GONE
        } else {
            binding.homeLayoutEmptyList.visibility = View.GONE
            binding.homeRecyclerChat.visibility = View.VISIBLE
        }
    }

    fun clearHistoricChat() {
        if (::adapterHistoric.isInitialized) {
            adapterHistoric.clear()
            historicChatList.clear()
        }
    }

}