package com.example.falaai.webclient

import com.example.falaai.webclient.services.ChatService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

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
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit = Retrofit.Builder().baseUrl("https://api.openai.com/v1/chat/")
        .addConverterFactory(MoshiConverterFactory.create()).client(client).build()

    val chatService: ChatService = retrofit.create(ChatService::class.java)

}