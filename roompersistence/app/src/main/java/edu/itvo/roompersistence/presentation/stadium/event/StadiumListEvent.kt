package edu.itvo.roompersistence.presentation.stadium.event

import edu.itvo.roompersistence.domain.model.Stadium

sealed class StadiumListEvent {
    data class DeleteStadium(val stadium: Stadium) : StadiumListEvent()
}
