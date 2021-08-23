package com.example.showtweetsonmap


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming

private const val BASE_TODO_URL = "https://api.twitter.com/2/tweets/search/stream/"


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_TODO_URL)
    .build()


interface TwitterApiService {

    @GET("rules/")
    suspend fun getAllStreamTweets(@Header("Authorization") token: String): Response<ResponseBody>
}

object TwitterApi {
    val RETROFIT_API: TwitterApiService by lazy { retrofit.create(TwitterApiService::class.java) }
}