package edu.itvo.roompersistence.presentation.stadium.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.itvo.roompersistence.domain.usecase.StadiumUseCases
import edu.itvo.roompersistence.presentation.stadium.event.StadiumListEvent
import edu.itvo.roompersistence.presentation.stadium.state.StadiumListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StadiumListViewModel @Inject constructor(
    private val useCases: StadiumUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(StadiumListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeStadiums()
    }

    fun onEvent(event: StadiumListEvent) {
        when (event) {
            is StadiumListEvent.DeleteStadium -> {
                deleteStadium(event)
            }
        }
    }

    private fun observeStadiums() {
        viewModelScope.launch {
            useCases.getStadiums().collect { stadiums ->
                _uiState.update {
                    it.copy(stadiums = stadiums)
                }
            }
        }
    }

    private fun deleteStadium(event: StadiumListEvent.DeleteStadium) {
        viewModelScope.launch {
            useCases.deleteStadium(event.stadium)
        }
    }
}
