package com.example.falaai.webclient

import com.example.falaai.webclient.services.ChatService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitLauncher {

    private val client by lazy {
        val yourAccessToken = "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"
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $yourAccessToken")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(logging)
            .build()
    }

    private val retrofit = Retrofit.Builder().baseUrl("https://api.openai.com/v1/chat/")
        .addConverterFactory(MoshiConverterFactory.create()).client(client).build()

    val chatService = retrofit.create(ChatService::class.java)

}