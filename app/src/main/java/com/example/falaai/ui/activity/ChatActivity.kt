package com.example.falaai.ui.activity

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.falaai.R
import com.example.falaai.constant.Constants.Companion.KEY_ASSISTANT
import com.example.falaai.constant.Constants.Companion.KEY_ERROR_MESSAGE
import com.example.falaai.constant.Constants.Companion.KEY_OPEN_CHAT
import com.example.falaai.constant.Constants.Companion.KEY_USER
import com.example.falaai.constant.EnumAlertType
import com.example.falaai.databinding.ActivityChatBinding
import com.example.falaai.extension.getTitle
import com.example.falaai.extension.setStatusBarTransparent
import com.example.falaai.extension.showMessage
import com.example.falaai.model.ModelChat
import com.example.falaai.model.ModelMessage
import com.example.falaai.storage.ChatStorage
import com.example.falaai.ui.adapter.AdapterChat
import com.example.falaai.webclient.RetrofitLauncher
import com.example.falaai.webclient.model.ChatRequest
import com.example.falaai.webclient.model.ChatResponse
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterChat
    private var chat: ModelChat = ModelChat()
    private val chatService = RetrofitLauncher().chatService
    private var isRunningRequest = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        setStatusBarTransparent()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(view)
        setupIntent()
        setupRecyclerView()
        setupButtonSendMessage()
        setupButtonBack()

    }

    private fun setupButtonBack() {
        binding.chatIconBack.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        checkEmptyList()
    }

    private fun setupIntent() {
        intent?.let { intent ->
            if (intent.hasExtra(KEY_OPEN_CHAT)) {
                val modelChat = intent.getSerializableExtra(KEY_OPEN_CHAT) as? ModelChat
                if (modelChat != null) {
                    chat = modelChat
                    chat.getTitle()?.let { binding.chatLabelTitle.text = it }
                } else {
                    checkEmptyList()
                    // Trate o caso em que o objeto é nulo
                }
            }
        }
    }

    private fun setupRecyclerView() {
        recyclerView = binding.chatRecycler
        adapter = AdapterChat(this, chat.messages)
        recyclerView.adapter = adapter
    }

    private fun setupButtonSendMessage() {
        binding.chatOutlinedTextField.setEndIconOnClickListener {
            val inputText = binding.chatOutlinedTextField.editText?.text.toString()
            hideKeyboard()
            binding.chatOutlinedTextField.editText?.setText("")
            if (isNetworkConnected()) {
                if (inputText.isNotBlank() && !isRunningRequest) {
                    isRunningRequest = true
                    val newMessage = ModelMessage(
                        role = KEY_USER, content = inputText
                    )
                    sendNewMessage(newMessage)
                    checkEmptyList()
                }
                recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
            } else {
                this.showMessage("Sem conexão!", EnumAlertType.NO_CONNECTION)
            }
        }
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    private fun hideKeyboard() {
        val editText = binding.chatInlinedTextField
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    private fun sendNewMessage(newMessage: ModelMessage) {
        chat.messages.add(newMessage)
        val chatCompletionRequest = ChatRequest(
            messages = chat.messages
        )
        val call = chatService.sendMessage(chatCompletionRequest)
        call.enqueue(object : Callback<ChatResponse> {

            override fun onResponse(
                call: Call<ChatResponse>,
                response: Response<ChatResponse>,
            ) {
                if (response.isSuccessful) {
                    onResponseMessage(response)
                } else {
                    treatErrorBody(response)
                }
                isRunningRequest = false
            }

            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                treatOnFailure(t)
            }

        })
        adapter.addLoadingMessage()
    }

    private fun onResponseMessage(response: Response<ChatResponse>) {
        val chatResponse = response.body()
        Log.i(
            "raeldev",
            "onResponse Resume Chat: ${chatResponse?.choices?.get(0)?.message?.content.toString()}"
        )
        val responseMessage = ModelMessage(
            role = KEY_ASSISTANT,
            content = chatResponse?.choices?.get(0)?.message?.content.toString()
        )
        adapter.addReceiveMessage(responseMessage)
        recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
        ChatStorage(this@ChatActivity).saveItem(chat)
    }

    private fun treatOnFailure(t: Throwable) {
        val responseMessage = ModelMessage(
            role = KEY_ERROR_MESSAGE, content = t.message.toString()
        )
        adapter.addErrorMessage(responseMessage)
        Log.i("raeldev", "onFailure: falha request ${t.message.toString()}")
        isRunningRequest = false
        recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
    }

    private fun treatErrorBody(response: Response<ChatResponse>) {
        val errorBody = response.errorBody()?.string()
        errorBody?.let {
            val responseMessage = ModelMessage(
                role = KEY_ERROR_MESSAGE, content = errorBody.toString()
            )
            adapter.addErrorMessage(responseMessage)
            recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
        }
        Log.i("raeldev", "onResponse: erro response $errorBody")
    }

    private fun checkEmptyList() {
        if (adapter.itemCount == 0) {
            binding.chatLayoutEmptyList.visibility = View.VISIBLE
            binding.chatRecycler.visibility = View.GONE
        } else {
            binding.chatLayoutEmptyList.visibility = View.GONE
            binding.chatRecycler.visibility = View.VISIBLE
            recyclerView.scrollToPosition(adapter.itemCount - 1)
        }
    }


}