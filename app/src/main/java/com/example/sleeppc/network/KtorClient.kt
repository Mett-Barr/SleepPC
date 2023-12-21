package com.example.sleeppc.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

class KtorClient(private val ip: String) {

    private suspend fun sleepPcCommand(): HttpResponse {
        return HttpClient(Android).post("http://$ip/sleep").also { httpResponse ->
            Log.d("Ktor", "HTTP response status: ${httpResponse.status.value}")
            val body = httpResponse.bodyAsText() // 获取响应的内容
            Log.d("Ktor", "HTTP response body: $body")
        }
    }

    suspend fun sleepPc(
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) = withContext(Dispatchers.Main) {
        if (sleepPcCommand().isSuccessful) {
            onSuccess()
        } else {
            onError()
        }
    }

    suspend fun sleepPcWithTimeout(
        onSuccess: () -> Unit,
        onError: () -> Unit,
        onTimeout: () -> Unit,
        timeoutMillis: Long
    ) {
        withTimeoutOrNull(timeoutMillis) {
            if (sleepPcCommand().isSuccessful) {
                onSuccess()
            } else {
                onError()
            }
        } ?: onTimeout()
    }

    private val HttpResponse.isSuccessful: Boolean
        get() = status.value in 200..299
}