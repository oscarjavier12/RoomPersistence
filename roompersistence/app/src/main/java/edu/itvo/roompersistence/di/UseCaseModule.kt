package edu.itvo.roompersistence.di

import edu.itvo.roompersistence.domain.repository.PlayerRepository
import edu.itvo.roompersistence.domain.usecase.AddPlayerUseCase
import edu.itvo.roompersistence.domain.usecase.GetPlayersUseCase
import edu.itvo.roompersistence.domain.usecase.PlayerUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.itvo.roompersistence.domain.usecase.DeletePlayerUseCase

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun providePlayerUseCases(
        repository: PlayerRepository
    ): PlayerUseCases {

        return PlayerUseCases(
            addPlayer = AddPlayerUseCase(repository),
            getPlayers = GetPlayersUseCase(repository),
            deletePlayer = DeletePlayerUseCase(repository)
        )
    }
}