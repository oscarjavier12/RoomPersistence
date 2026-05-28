package edu.itvo.roompersistence.domain.usecase

data class StadiumUseCases(
    val addStadium: AddStadiumUseCase,
    val updateStadium: UpdateStadiumUseCase,
    val getStadiums: GetStadiumsUseCase,
    val getStadiumById: GetStadiumByIdUseCase,
    val deleteStadium: DeleteStadiumUseCase
)
