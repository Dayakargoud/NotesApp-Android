package com.dayakar.simplenote.di

import com.dayakar.simplenote.database.NoteDatabase
import com.dayakar.simplenote.network.NotesAPI
import com.dayakar.simplenote.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(database:NoteDatabase,notesAPI: NotesAPI):MainRepository{
        return MainRepository(database,notesAPI)

    }
}