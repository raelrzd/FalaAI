package com.example.falaai

import android.app.Application
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.client.OpenAI
import kotlin.time.Duration.Companion.seconds

class MyApplication : Application() {

    companion object {
        val instance: MyApplication = MyApplication()
    }

    private val openAI: OpenAI by lazy {
        OpenAI(
            token = "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",
            timeout = Timeout(socket = 60.seconds)
        )
    }

    fun getOpenAIService(): OpenAI {
        return openAI
    }

}