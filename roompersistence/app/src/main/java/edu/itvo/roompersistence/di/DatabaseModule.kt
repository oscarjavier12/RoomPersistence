package edu.itvo.roompersistence.di

import android.content.Context
import androidx.room.Room
import edu.itvo.roompersistence.data.local.dao.PlayerDao
import edu.itvo.roompersistence.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "player_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providePlayerDao(
        database: AppDatabase
    ): PlayerDao {

        return database.playerDao()
    }
}