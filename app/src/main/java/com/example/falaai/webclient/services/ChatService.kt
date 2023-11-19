package com.example.falaai.webclient.services
import com.example.falaai.webclient.model.ChatRequest
import com.example.falaai.webclient.model.ChatResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatService {

    @POST("completions")
    fun sendMessage(@Body request: ChatRequest): Call<ChatResponse>
}
