package com.example.falaai.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val MODEL_CHAT_REQUEST = "gpt-3.5-turbo"

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
        recyclerView = binding.recyclerChat
        adapter = AdapterChat(this, chat.chat)
        recyclerView.adapter = adapter


        setupButtonSendMessage()

        binding.chatBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

    }

    private fun setupButtonSendMessage() {
        binding.outlinedTextField.setEndIconOnClickListener {
            val inputText = binding.outlinedTextField.editText?.text.toString()
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
                            } else {
                                val errorBody = response.errorBody()?.string()
                                Log.i("raeldev", "onResponse: erro $errorBody")
                            }
                        }

                        override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                            Log.i("raeldev", "onFailure: falhaa")
                        }

                    })
                    ChatStorage(this@ChatActivity).updateItem(chat)
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
                                Log.i(
                                    "raeldev",
                                    "onCreate: ${chatResponse?.choices?.get(0)?.message?.content.toString()}"
                                )
                                val responseMessage = ModelMessage(
                                    role = KEY_ASSISTANT,
                                    content = chatResponse?.choices?.get(0)?.message?.content.toString()
                                )
                                chat!!.chat.add(responseMessage)
                                adapter.addMessage(responseMessage)
                            } else {
                                val errorBody = response.errorBody()?.string()
                                Log.i("raeldev", "onResponse: erro $errorBody")
                                // Trate erros aqui
                            }
                        }

                        override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                            // Trate falhas na chamada aqui
                            Log.i("raeldev", "onFailure: falhaa")
                        }

                    })
                    ChatStorage(this@ChatActivity).addItem(chat!!)
                }
            }
        }
    }

    private fun setupIntent() {
        intent?.let { intent ->
            if (intent.hasExtra(KEY_OPEN_CHAT)) {
                val jsonChat = intent.getStringExtra(KEY_OPEN_CHAT)
                chat = Gson().fromJson(jsonChat, object : TypeToken<ModelChat>() {}.type)
            }
        }
    }

}