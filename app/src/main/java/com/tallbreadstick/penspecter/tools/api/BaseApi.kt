package com.tallbreadstick.penspecter.tools.api

import io.github.cdimascio.dotenv.dotenv
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

val ShodanBaseUrl: String = "https://api.shodan.io/"

fun getApiKey(): String {
    val dotenv = dotenv {
        directory = "/assets"
        filename = "env"
    }
    return dotenv["SHODAN_API_KEY"]
}

interface DNSLookupService {
    @GET("dns/resolve")
    suspend fun resolveDns(
        @Query("hostnames") hostnames: String,
        @Query("key") apiKey: String
    ): Map<String, String>
}

object RetrofitClient {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(ShodanBaseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService: DNSLookupService = retrofit.create(DNSLookupService::class.java)
}