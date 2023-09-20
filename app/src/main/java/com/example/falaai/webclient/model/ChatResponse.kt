package com.example.falaai.webclient.model

import com.example.falaai.model.ModelMessage

data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val finish_reason: String,
    val index: Int,
    val message: ModelMessage
)