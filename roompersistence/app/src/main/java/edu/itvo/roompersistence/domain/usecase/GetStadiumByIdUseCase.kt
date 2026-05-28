package edu.itvo.roompersistence.domain.usecase

import edu.itvo.roompersistence.domain.model.Stadium
import edu.itvo.roompersistence.domain.repository.StadiumRepository
import javax.inject.Inject

class GetStadiumByIdUseCase @Inject constructor(
    private val repository: StadiumRepository
) {
    suspend operator fun invoke(id: Long): Stadium? {
        return repository.getStadiumById(id)
    }
}
