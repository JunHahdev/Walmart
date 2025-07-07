package com.example.listofcountries.data.repository

import com.example.listofcountries.data.api.CountryAPIService
import com.example.listofcountries.data.model.CountryItem

class CountryAPIRepositoryImpl(
    private val apiService: CountryAPIService
) : CountryAPIRepository {
    override suspend fun getCountries(): List<CountryItem> = apiService.getCountries()
}
