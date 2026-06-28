package com.example.moviedb.core.network

import com.example.moviedb.core.error.InternetUnavailable
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkInterceptor @Inject constructor(
    private val connectivityObserver: ConnectivityObserver
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!connectivityObserver.isConnected.value) {
            throw InternetUnavailable()
        }

        return chain.proceed(chain.request())
    }
}

@Singleton
class AuthInterceptor @Inject constructor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        requestBuilder.addHeader("Authorization", "Bearer ${Configs.API_KEY}")
        requestBuilder.addHeader("accept", "application/json")

        return chain.proceed(requestBuilder.build())
    }
}