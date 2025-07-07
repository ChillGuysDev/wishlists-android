package com.nikol.wishlist.core.data.network

import com.nikol.wishlist.core.data.BuildConfig
import com.nikol.wishlist.core.domain.tokenprovider.AuthTokenProvider
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitClient @Inject constructor(
    private val tokenStorage: AuthTokenProvider
) {

    companion object {
        private const val BASE_URL = BuildConfig.BASE_URL
        private const val CLEAN_URL = BuildConfig.CLEAN_URL
        private const val TIMEOUT = 30L
    }

    // Configure JSON serialization
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
        isLenient = true
    }

    // Authentication interceptor to add token to requests
    private val authInterceptor = Interceptor { chain ->
        val token = runBlocking { tokenStorage.getAccessToken() }
        if (token != null) {
            val newRequest = chain.request().newBuilder()
                .header("Authorization", "Bearer $token")
                .build()

            return@Interceptor chain.proceed(newRequest)
        } else {
            return@Interceptor chain.proceed(chain.request())
        }
    }

    // Create logging interceptor for debugging
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val certificatePinner = CertificatePinner.Builder()
        .add(CLEAN_URL, "sha256/1EkvzibgiE3k+xdsv+7UU5vhV8kdFCQiUiFdMX5Guuk=")
        .add(CLEAN_URL, "sha256/Horp780Uhh2eFpKgkqHns9yuN+xF9W9hLNMxwxyU5PU=")
        .build()

    private val tempOkHttp = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .certificatePinner(certificatePinner)
        .hostnameVerifier { _, _ -> true }
        .build()

    private val tempRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(tempOkHttp)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val authApiService = tempRetrofit.create(AuthApiService::class.java)
    private val authenticator = Authenticator { _, response ->

        val oldRefreshToken = runBlocking { tokenStorage.getRefreshToken() }
        if (oldRefreshToken == null) return@Authenticator null

        val (access, refresh) = authApiService.refreshToken(RefreshTokenBody(oldRefreshToken))
            .execute().body() ?: return@Authenticator null
        runBlocking { tokenStorage.saveTokens(access, refresh) }

        val newToken = runBlocking { tokenStorage.getAccessToken() }
        return@Authenticator if (newToken != null) {
            response.request.newBuilder()
                .header("Authorization", "Bearer $newToken")
                .build()
        } else {
            null
        }
    }


    // Configure OkHttpClient with interceptors
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .certificatePinner(certificatePinner)
        .hostnameVerifier { _, _ -> true }
        .authenticator(authenticator)
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .build()

    // Create Retrofit instance with the OkHttpClient
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()


    // Function to create API services
    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}
