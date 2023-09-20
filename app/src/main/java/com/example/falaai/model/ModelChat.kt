package com.example.falaai.model

import java.util.UUID

class ModelChat(
    val id: String = UUID.randomUUID().toString(),
    var title: String? = null,
    var chat: MutableList<ModelMessage> = mutableListOf(),
)