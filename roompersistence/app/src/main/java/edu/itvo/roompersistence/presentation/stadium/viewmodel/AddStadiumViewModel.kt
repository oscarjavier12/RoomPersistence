package edu.itvo.roompersistence.presentation.stadium.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.itvo.roompersistence.domain.model.Stadium
import edu.itvo.roompersistence.domain.usecase.StadiumUseCases
import edu.itvo.roompersistence.presentation.stadium.event.AddStadiumEvent
import edu.itvo.roompersistence.presentation.stadium.event.AddStadiumUiEvent
import edu.itvo.roompersistence.presentation.stadium.state.AddStadiumUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddStadiumViewModel @Inject constructor(
    private val useCases: StadiumUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddStadiumUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<AddStadiumUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: AddStadiumEvent) {
        when (event) {
            is AddStadiumEvent.OnNameChanged -> {
                _uiState.update { it.copy(name = event.value) }
            }
            is AddStadiumEvent.OnCityChanged -> {
                _uiState.update { it.copy(city = event.value) }
            }
            is AddStadiumEvent.OnCountryChanged -> {
                _uiState.update { it.copy(country = event.value) }
            }
            is AddStadiumEvent.OnCapacityChanged -> {
                _uiState.update { it.copy(capacity = event.value) }
            }
            is AddStadiumEvent.OnYearBuiltChanged -> {
                _uiState.update { it.copy(yearBuilt = event.value) }
            }
            is AddStadiumEvent.OnSurfaceChanged -> {
                _uiState.update { it.copy(surface = event.value) }
            }
            AddStadiumEvent.SaveStadium -> {
                saveStadium()
            }
        }
    }

    fun loadStadium(stadiumId: Long) {
        viewModelScope.launch {
            useCases.getStadiumById(stadiumId)?.let { stadium ->
                _uiState.update {
                    it.copy(
                        id = stadium.id,
                        name = stadium.name,
                        city = stadium.city,
                        country = stadium.country,
                        capacity = stadium.capacity.toString(),
                        yearBuilt = stadium.yearBuilt.toString(),
                        surface = stadium.surface
                    )
                }
            }
        }
    }

    private fun saveStadium() {
        viewModelScope.launch {
            val state = uiState.value

            if (state.name.isBlank()) {
                _uiEvent.send(
                    AddStadiumUiEvent.ShowSnackbar("Stadium name is required")
                )
                return@launch
            }

            if (state.city.isBlank()) {
                _uiEvent.send(
                    AddStadiumUiEvent.ShowSnackbar("City is required")
                )
                return@launch
            }

            val capacity = state.capacity.toIntOrNull()
            if (capacity == null || capacity <= 0) {
                _uiEvent.send(
                    AddStadiumUiEvent.ShowSnackbar("Enter a valid capacity")
                )
                return@launch
            }

            val yearBuilt = state.yearBuilt.toIntOrNull()
            if (yearBuilt == null || yearBuilt < 1800) {
                _uiEvent.send(
                    AddStadiumUiEvent.ShowSnackbar("Enter a valid year (>= 1800)")
                )
                return@launch
            }

            val stadium = Stadium(
                id = state.id ?: 0L,
                name = state.name,
                city = state.city,
                country = state.country,
                capacity = capacity,
                yearBuilt = yearBuilt,
                surface = state.surface
            )

            if (state.id == null) {
                useCases.addStadium(stadium)
            } else {
                useCases.updateStadium(stadium)
            }

            _uiEvent.send(
                AddStadiumUiEvent.ShowSnackbar(
                    message = "Stadium saved successfully",
                    navigateBack = true
                )
            )
        }
    }
}
