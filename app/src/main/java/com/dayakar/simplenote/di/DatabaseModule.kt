package com.dayakar.simplenote.di

import android.content.Context
import androidx.room.Room
import com.dayakar.simplenote.database.NoteDatabase
import com.dayakar.simplenote.database.NotesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideNoteDatabase(@ApplicationContext context: Context):NoteDatabase{
        return Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME).fallbackToDestructiveMigration().build()


    }
    @Singleton
    @Provides
    fun provideNoteDao(noteDatabase: NoteDatabase):NotesDao{
        return noteDatabase.notesDao()

    }

}