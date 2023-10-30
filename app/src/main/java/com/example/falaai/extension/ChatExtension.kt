package com.example.falaai.extension

import com.example.falaai.model.ModelChat

fun ModelChat.getTitle(): String? {
    if (this.messages.isNotEmpty()) {
        return this.messages[0].content
    }
    return null
}