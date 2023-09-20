package com.example.falaai.model

import com.aallam.openai.api.chat.ChatMessage
import java.util.UUID

class ModelChat(val id: String = UUID.randomUUID().toString(), var title: String? = null, var chat: MutableList<ChatMessage> = mutableListOf())