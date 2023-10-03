package com.example.falaai.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.falaai.constants.Constants.Companion.KEY_ASSISTANT
import com.example.falaai.constants.Constants.Companion.KEY_OPEN_CHAT
import com.example.falaai.constants.Constants.Companion.KEY_USER
import com.example.falaai.databinding.ActivityChatBinding
import com.example.falaai.model.ModelChat
import com.example.falaai.model.ModelMessage
import com.example.falaai.storage.ChatStorage
import com.example.falaai.ui.adapter.AdapterChat
import com.example.falaai.webclient.RetrofitLauncher
import com.example.falaai.webclient.model.ChatRequest
import com.example.falaai.webclient.model.ChatResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterChat
    private var chat: ModelChat = ModelChat()
    private val chatService = RetrofitLauncher().chatService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupIntent()
        recyclerView = binding.chatRecycler
        adapter = AdapterChat(this, chat.chat)
        recyclerView.adapter = adapter


        setupButtonSendMessage()


        binding.outlinedTextField.editText?.addTextChangedListener {
//            Toast.makeText(this, "texto alterado", Toast.LENGTH_SHORT).show()
        }

        binding.chatIconBack.setOnClickListener {
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        checkEmptyList()
    }

    private fun setupButtonSendMessage() {
        binding.outlinedTextField.setEndIconOnClickListener {
            val inputText = binding.outlinedTextField.editText?.text.toString()
            if (inputText != "") {
                val newMessage = ModelMessage(
                    role = KEY_USER,
                    content = inputText
                )
                adapter.addMessage(newMessage)
                lifecycleScope.launch {
                    ChatStorage(this@ChatActivity).getItem(chat)?.let {
                        chat.chat.add(newMessage)
                        val chatCompletionRequest = ChatRequest(
                            messages = chat.chat
                        )
                        val call = chatService.sendMessage(chatCompletionRequest)
                        call.enqueue(object : Callback<ChatResponse> {

                            override fun onResponse(
                                call: Call<ChatResponse>,
                                response: Response<ChatResponse>,
                            ) {
                                if (response.isSuccessful) {
                                    val chatResponse = response.body()
                                    Log.i("raeldev", "onResponse: $chatResponse")
                                    Log.i(
                                        "raeldev",
                                        "onResponse: ${chatResponse?.choices?.get(0)?.message?.content.toString()}"
                                    )
                                    val responseMessage = ModelMessage(
                                        role = KEY_ASSISTANT,
                                        content = chatResponse?.choices?.get(0)?.message?.content.toString()
                                    )
                                    chat.chat.add(responseMessage)
                                    adapter.addMessage(responseMessage)
                                    ChatStorage(this@ChatActivity).updateItem(chat)
                                } else {
                                    val errorBody = response.errorBody()?.string()
                                    Log.i("raeldev", "onResponse: erro $errorBody")
                                }
                            }

                            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                                Log.i("raeldev", "onFailure: falhaa")
                            }

                        })
                    } ?: run {
                        chat = ModelChat(chat = mutableListOf(newMessage))
                        val chatCompletionRequest = ChatRequest(
                            messages = listOf(
                                newMessage
                            )
                        )
                        val call = chatService.sendMessage(chatCompletionRequest)
                        call.enqueue(object : Callback<ChatResponse> {

                            override fun onResponse(
                                call: Call<ChatResponse>,
                                response: Response<ChatResponse>,
                            ) {
                                if (response.isSuccessful) {
                                    val chatResponse = response.body()
                                    Log.i("raeldev", "onResponse: $chatResponse")
                                    Log.i(
                                        "raeldev",
                                        "onCreate: ${chatResponse?.choices?.get(0)?.message?.content.toString()}"
                                    )
                                    val responseMessage = ModelMessage(
                                        role = KEY_ASSISTANT,
                                        content = chatResponse?.choices?.get(0)?.message?.content.toString()
                                    )
                                    chat.chat.add(responseMessage)
                                    adapter.addMessage(responseMessage)
                                    ChatStorage(this@ChatActivity).addItem(chat)
                                } else {
                                    val errorBody = response.errorBody()?.string()
                                    Log.i("raeldev", "onResponse: erro $errorBody")
                                    // Trate erros aqui
                                }
                            }

                            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                                // Trate falhas na chamada aqui
                                Log.i("raeldev", "onFailure: falhaa $t  ${call.isExecuted}")
                            }

                        })

                    }
                }
                binding.outlinedTextField.editText?.setText("")
                checkEmptyList()
            }
        }
    }

    private fun checkEmptyList() {
        if (adapter.itemCount == 0) {
            binding.chatLayoutEmptyList.visibility = View.VISIBLE
            binding.chatRecycler.visibility = View.GONE
        } else {
            binding.chatLayoutEmptyList.visibility = View.GONE
            binding.chatRecycler.visibility = View.VISIBLE
        }
    }

    private fun setupIntent() {
        intent?.let { intent ->
            if (intent.hasExtra(KEY_OPEN_CHAT)) {
                val modelChat = intent.getSerializableExtra(KEY_OPEN_CHAT) as? ModelChat
                if (modelChat != null) {
                    chat = modelChat
                } else {
                    checkEmptyList()
                    // Trate o caso em que o objeto é nulo
                }
            }
        }
    }

}