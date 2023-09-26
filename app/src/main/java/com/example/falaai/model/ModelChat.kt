package com.example.falaai.model

import java.io.Serializable
import java.util.UUID

data class ModelChat(
    val id: String = UUID.randomUUID().toString(),
    var title: String? = null,
    var chat: MutableList<ModelMessage> = mutableListOf(),
) : Serializable