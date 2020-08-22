package com.dayakar.simplenote.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NoteDatabaseEntity::class],version = 1,exportSchema = false)
abstract class NoteDatabase:RoomDatabase(){

    abstract fun notesDao():NotesDao

    companion object{
        const val DATABASE_NAME:String="notes_db"
    }
}
