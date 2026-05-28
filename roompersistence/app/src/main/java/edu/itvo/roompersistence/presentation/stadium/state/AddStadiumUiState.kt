package edu.itvo.roompersistence.presentation.stadium.state

data class AddStadiumUiState(
    val id: Long? = null,
    val name: String = "",
    val city: String = "",
    val country: String = "",
    val capacity: String = "",
    val yearBuilt: String = "",
    val surface: String = ""
)
