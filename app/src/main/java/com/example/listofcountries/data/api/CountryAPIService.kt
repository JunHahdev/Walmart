package com.example.listofcountries.data.api

import com.example.listofcountries.data.model.CountryItem
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CountryAPIService {

    @GET(APIDetail.END_POINT)
    suspend fun getCountries(): List<CountryItem>

    companion object {
        fun create(): CountryAPIService {
            val retrofit = Retrofit.Builder()
                .baseUrl(APIDetail.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(CountryAPIService::class.java)
        }
    }


}