package com.example.falaai.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.example.falaai.MyApplication
import com.example.falaai.constant.Constants.Companion.KEY_OPEN_CHAT
import com.example.falaai.databinding.ActivityChatBinding
import com.example.falaai.model.ModelChat
import com.example.falaai.storage.ChatStorage
import com.example.falaai.ui.adapter.AdapterChat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

private const val MODEL_CHAT_REQUEST = "gpt-3.5-turbo"

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val openAIService by lazy {
        MyApplication.instance.getOpenAIService()
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterChat
    private var chat: ModelChat? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupIntent()
        recyclerView = binding.recyclerChat
        adapter = AdapterChat(this, chat?.chat ?: mutableListOf())
        recyclerView.adapter = adapter


        setupButtonSendMessage()

        binding.chatBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

    }

    private fun setupButtonSendMessage() {
        binding.outlinedTextField.setEndIconOnClickListener {
            val inputText = binding.outlinedTextField.editText?.text.toString()
            val newMessage = ChatMessage(
                role = ChatRole.User,
                content = inputText
            )
            adapter.addMessage(newMessage)
            lifecycleScope.launch {
                if (chat != null) {
                    chat!!.chat.add(newMessage)
                    val chatCompletionRequest = ChatCompletionRequest(
                        model = ModelId(MODEL_CHAT_REQUEST),
                        messages = chat!!.chat
                    )
                    openAIService.chatCompletion(chatCompletionRequest).let { chatCompletion ->
                        Log.i(
                            "raeldev",
                            "onCreate: ${chatCompletion.choices[0].message.content.toString()}"
                        )
                        val responseMessage = ChatMessage(
                            role = ChatRole.Assistant,
                            content = chatCompletion.choices[0].message.content.toString()
                        )
                        chat!!.chat.add(responseMessage)
                        adapter.addMessage(responseMessage)
                    }
                    ChatStorage(this@ChatActivity).updateItem(chat!!)
                } else {
                    chat = ModelChat(chat = mutableListOf(newMessage))
                    val chatCompletionRequest = ChatCompletionRequest(
                        model = ModelId(MODEL_CHAT_REQUEST),
                        messages = listOf(
                            newMessage
                        )
                    )
                    openAIService.chatCompletion(chatCompletionRequest).let { chatCompletion ->
                        chatCompletion.choices[0].message
                        Log.i(
                            "raeldev",
                            "onCreate: ${chatCompletion.choices[0].message.content.toString()}"
                        )
                        val responseMessage = ChatMessage(
                            ChatRole.Assistant,
                            content = chatCompletion.choices[0].message.content.toString()
                        )
                        chat!!.chat.add(responseMessage)
                        adapter.addMessage(responseMessage)
                    }
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