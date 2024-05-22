package com.android.myapplication.apis

import com.android.myapplication.utils.MyUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(MyUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        instance.create(ApiService::class.java)
    }
}