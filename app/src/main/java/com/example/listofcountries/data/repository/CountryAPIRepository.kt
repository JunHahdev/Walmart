package com.example.listofcountries.data.repository

import com.example.listofcountries.data.model.CountryItem

interface CountryAPIRepository {

    suspend fun getCountries(): List<CountryItem>
}