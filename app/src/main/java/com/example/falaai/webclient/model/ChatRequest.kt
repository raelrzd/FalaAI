package com.example.falaai.webclient.model

import com.example.falaai.model.ModelMessage

class ChatRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<ModelMessage>
)