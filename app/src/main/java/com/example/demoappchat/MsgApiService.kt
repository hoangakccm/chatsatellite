package com.example.demoappchat

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface MsgApiService {

    @POST("send-message")
    fun sendMessage(
        @Header("username") username: String,
        @Header("password") password: String,
        @Body messageData: MessageData
    ): Call<Void>

    @POST("user/messages")
    fun getUserMessages(
        @Header("username") username: String,
        @Header("password") password: String,
        @Body timeRange: TimeRange
    ): Call<ResponseBody>

    @POST("login")
    fun login(
        @Body requestBody: LoginInfo
    ): Call<LoginResponse>
}
object RetrofitClient {
    private const val BASE_URL = "http://nhatkydientu.vn:5000/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val instance: MsgApiService by lazy {
        retrofit.create(MsgApiService::class.java)
    }
}
