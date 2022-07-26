package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Json
import com.udacity.asteroidradar.PictureOfDay
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL = "https://api.nasa.gov/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()


interface AsteroidAPIService{
    @GET("/planetary/apod")
    suspend fun getImageOfTheDay(@Query("api_key") api_key:String = "ix0YEJjyegCmQpyZ310xZTV7fI4tlA9fDJYp6cAH"):PictureOfDay

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(@Query("api_key") apiKey:String= "ix0YEJjyegCmQpyZ310xZTV7fI4tlA9fDJYp6cAH"):String
}

object AsteroidAPI{
    val retrofitService :AsteroidAPIService by lazy {
        retrofit.create(AsteroidAPIService::class.java)
    }

}
