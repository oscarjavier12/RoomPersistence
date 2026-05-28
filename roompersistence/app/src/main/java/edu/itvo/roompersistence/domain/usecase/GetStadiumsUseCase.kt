package edu.itvo.roompersistence.domain.usecase

import edu.itvo.roompersistence.domain.model.Stadium
import edu.itvo.roompersistence.domain.repository.StadiumRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStadiumsUseCase @Inject constructor(
    private val repository: StadiumRepository
) {
    operator fun invoke(): Flow<List<Stadium>> {
        return repository.getStadiums()
    }
}
