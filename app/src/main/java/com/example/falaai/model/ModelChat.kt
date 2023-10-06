package com.example.falaai.model

import java.io.Serializable
import java.util.UUID

data class ModelChat(
    val id: String = UUID.randomUUID().toString(),
    var title: String? = null,
    var messages: MutableList<ModelMessage> = mutableListOf(),
) : Serializable