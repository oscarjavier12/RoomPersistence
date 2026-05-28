package edu.itvo.roompersistence.presentation.stadium.event

sealed class AddStadiumEvent {
    data class OnNameChanged(val value: String) : AddStadiumEvent()
    data class OnCityChanged(val value: String) : AddStadiumEvent()
    data class OnCountryChanged(val value: String) : AddStadiumEvent()
    data class OnCapacityChanged(val value: String) : AddStadiumEvent()
    data class OnYearBuiltChanged(val value: String) : AddStadiumEvent()
    data class OnSurfaceChanged(val value: String) : AddStadiumEvent()
    data object SaveStadium : AddStadiumEvent()
}
