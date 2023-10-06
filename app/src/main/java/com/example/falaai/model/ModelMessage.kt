package com.example.falaai.model

import java.io.Serializable

data class ModelMessage(
    var content: String,
    val role: String
) : Serializable