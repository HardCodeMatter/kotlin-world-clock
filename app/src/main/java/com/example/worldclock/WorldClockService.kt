package com.example.worldclock
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WorldClockService {
    @GET("timezone/Europe/{city}")
    fun getWorldTime(@Path("city") city: String): Call<WorldClockResponse>
}
