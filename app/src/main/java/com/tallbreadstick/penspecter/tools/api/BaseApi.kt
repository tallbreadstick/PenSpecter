package com.tallbreadstick.penspecter.tools.api

import com.tallbreadstick.penspecter.models.DomainInfoResponse
import com.tallbreadstick.penspecter.models.GeoLocationResponse
import io.github.cdimascio.dotenv.dotenv
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val ShodanBaseUrl: String = "https://api.shodan.io/"
const val LocationBaseUrl: String = "https://api.ipgeolocation.io/"
fun getShodanApiKey(): String {
    val dotenv = dotenv {
        directory = "/assets"
        filename = "env"
    }
    return dotenv["SHODAN_API_KEY"]
}

fun getLocationApiKey(): String {
    val dotenv = dotenv {
        directory = "/assets"
        filename = "env"
    }
    return dotenv["LOCATION_API_KEY"]
}

fun getMapsApiKey(): String {
    val dotenv = dotenv {
        directory = "/assets"
        filename = "env"
    }
    return dotenv["MAPS_API_KEY"]
}

interface DNSLookupService {
    @GET("dns/resolve")
    suspend fun resolveDns(
        @Query("hostnames") hostnames: String,
        @Query("key") apiKey: String
    ): Map<String, String>

    @GET("dns/reverse")
    suspend fun reverseDns(
        @Query("ips") ips: String,
        @Query("key") apiKey: String
    ): Map<String, List<String>>

    @GET("dns/domain/{domain}")
    suspend fun getDomainInfo(
        @Path("domain") domain: String,
        @Query("key") apiKey: String,
        @Query("history") history: Boolean = false,
        @Query("type") type: String? = null,
        @Query("page") page: Int = 1
    ): DomainInfoResponse

}

interface GeoLocationService {
    @GET("ipgeo")
    suspend fun getLocation(
        @Query("apiKey") apiKey: String,
        @Query("ip") ip: String
    ): GeoLocationResponse
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
    val locationRetrofit = Retrofit.Builder()
        .baseUrl(LocationBaseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val locationService: GeoLocationService = locationRetrofit.create(GeoLocationService::class.java)
    val apiService: DNSLookupService = retrofit.create(DNSLookupService::class.java)
}