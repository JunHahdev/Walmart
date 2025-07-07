package com.example.listofcountries.ui.viewmodel

import com.example.listofcountries.data.model.CountryItem
import com.example.listofcountries.data.model.Currency
import com.example.listofcountries.data.model.Language
import com.example.listofcountries.data.repository.CountryAPIRepository
import com.example.listofcountries.ui.state.UiState
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class CountryViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mockkRule = MockKRule(this)

    @Mock
    private lateinit var countryAPIRepository: CountryAPIRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCountries should emit success when repository returns data`() = runTest {
        val expectedCountries = listOf(
            CountryItem(
                capital = "Test Capital",
                code = "TC",
                currency = Currency(name = "Test Dollar", code = "TD", symbol = "$"),
                demonym = "Testian",
                flag = "flag",
                language = Language(
                    name = "Testish",
                    code = "TST",
                    iso639_2 = "test",
                    nativeName = "testName"
                ),
                name = "Testland",
                region = "Test Region"
            )
        )

        `when`(countryAPIRepository.getCountries()).thenReturn(expectedCountries)

        val viewModel = CountryViewModel(countryAPIRepository)

        val state = viewModel.uiState.first { it is UiState.Success }
        assertTrue(state is UiState.Success)
        assertEquals(expectedCountries, (state as UiState.Success).data)
    }

    @Test
    fun `getCountries should emit error when response is empty`() = runTest {
        val expectedCountries = emptyList<CountryItem>()

        `when`(countryAPIRepository.getCountries()).thenReturn(expectedCountries)

        val viewModel = CountryViewModel(countryAPIRepository)

        val state = viewModel.uiState.first { it is UiState.Error }
        assertTrue(state is UiState.Error)
        assertEquals("No countries found", (state as UiState.Error).message)
    }

    @Test
    fun `uiState should emit Loading initially before coroutine advances`() = runTest {
        `when`(countryAPIRepository.getCountries()).thenReturn(emptyList())

        val viewModel = CountryViewModel(countryAPIRepository)

        val state = viewModel.uiState.value
        assertTrue(state is UiState.Loading)

        testScheduler.advanceUntilIdle()
    }


}