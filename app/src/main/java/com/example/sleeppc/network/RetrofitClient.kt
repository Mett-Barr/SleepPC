package com.example.sleeppc.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query

class RetrofitClient(val context: Context, val ip: String) {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://$ip")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    suspend fun sleepPc() {

        Log.d("!!!", "sleepPc: ip = $ip")

        try {
            Log.d("!!!", "sleepPc: ip = $ip")
            apiService.sendSleepCommand()

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Sleep done", Toast.LENGTH_SHORT).show()
            }
            Log . d ("!!!", "commandTest  isSuccessful")
        } catch (httpException: HttpException) {
            Log.d("!!!", "commandTest  Handle error")
        } catch (t: Throwable) {
            Log.d("!!!", "commandTest  onFailure  $ip")
            Log.d("!!!", t.toString())

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Sleep failed", Toast.LENGTH_SHORT).show()
            }
        }

//        apiService.sendSleepCommand().enqueue(object : Callback<Void> {
//            override fun onResponse(call: Call<Void>, response: Response<Void>) {
//                if (response.isSuccessful) {
//                    // Handle success
//
//                    Toast.makeText(context, "Sleep done", Toast.LENGTH_SHORT).show()
//                    Log.d("!!!", "commandTest  isSuccessful")
//                } else {
//                    // Handle error
//                    Log.d("!!!", "commandTest  Handle error")
//                }
//            }
//
//            override fun onFailure(call: Call<Void>, t: Throwable) {
//                // Handle error
//                Log.d("!!!", "commandTest  onFailure  $ip")
//                Log.d("!!!", t.toString())
//
//
//                Toast.makeText(context, "Sleep failed", Toast.LENGTH_SHORT).show()
//            }
//        })



        suspend fun controlMedia(mediaCommand: MediaCommand) {
            try {
                apiService.sendMediaControlCommand(mediaCommand.command)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Media control: ${mediaCommand.command} done", Toast.LENGTH_SHORT).show()
                }
            } catch (httpException: HttpException) {
                Log.d("!!!", "Media control: Handle error")
            } catch (t: Throwable) {
                Log.d("!!!", "Media control: onFailure $ip")
                Log.d("!!!", t.toString())

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Media control: ${mediaCommand.command} failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}

enum class MediaCommand(val command: String) {
    PLAY_PAUSE("playpause"),
    VOLUME_UP("volumeup"),
    VOLUME_DOWN("volumedown"),
    NEXT("next"),
    PREVIOUS("previous")
}

interface ApiService {
    @POST("/sleep")
    suspend fun sendSleepCommand(): Response<Void>

    @POST("/media-control")
    suspend fun sendMediaControlCommand(@Query("command") command: String): Response<Void>
}