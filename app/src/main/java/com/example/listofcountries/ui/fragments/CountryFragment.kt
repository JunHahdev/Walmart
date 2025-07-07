package com.example.listofcountries.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listofcountries.data.api.CountryAPIService
import com.example.listofcountries.data.repository.CountryAPIRepositoryImpl
import com.example.listofcountries.databinding.FragmentCountryBinding
import com.example.listofcountries.ui.adapters.CountryAdapter
import com.example.listofcountries.ui.state.UiState
import com.example.listofcountries.ui.viewmodel.CountryViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CountryFragment : Fragment() {

    private var _binding: FragmentCountryBinding? = null
    private val binding get() = _binding!!


    private lateinit var countryAdapter: CountryAdapter

    private lateinit var countryViewModel: CountryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = CountryAPIRepositoryImpl(CountryAPIService.create())

        countryViewModel = ViewModelProvider(
            this,
            object: ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CountryViewModel(repository) as T
                }
            }
        )[CountryViewModel::class.java]

        countryAdapter = CountryAdapter()

        binding.rvCountries.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = countryAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            countryViewModel.uiState.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.progressBar.isVisible = true
                        binding.rvCountries.isVisible = false
                        binding.tvError.isVisible = false
                    }
                    is UiState.Success -> {
                        binding.progressBar.isVisible = false
                        binding.rvCountries.isVisible = true
                        binding.tvError.isVisible = false
                        countryAdapter.update(state.data)
                    }
                    is UiState.Error -> {
                        binding.progressBar.isVisible = false
                        binding.rvCountries.isVisible = false
                        binding.tvError.isVisible = true
                        binding.tvError.text = state.message
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
