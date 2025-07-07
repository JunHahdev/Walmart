package com.example.listofcountries.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listofcountries.data.model.CountryItem
import com.example.listofcountries.data.repository.CountryAPIRepository
import com.example.listofcountries.ui.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CountryViewModel(
    private val countryAPIRepository: CountryAPIRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<CountryItem>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<CountryItem>>> = _uiState

    init {
        getCountries()
    }

    private fun getCountries() {
        viewModelScope.launch(Dispatchers.IO){
            _uiState.value = UiState.Loading
            try {
                val result = countryAPIRepository.getCountries()
                if (result.isEmpty()) {
                    _uiState.value = UiState.Error("No countries found")
                } else {
                    _uiState.value = UiState.Success(result)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }
}
