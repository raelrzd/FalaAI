package com.example.falaai.model

import java.io.Serializable

data class ModelMessage(
    val content: String,
    val role: String
) : Serializable