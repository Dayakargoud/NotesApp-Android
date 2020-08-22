package com.dayakar.simplenote.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(noteDatabaseEntity: NoteDatabaseEntity):Long          //return type long tells which row is inserted

    @Query("select * from notes_table")
     fun getAllNotes():LiveData<List<NoteDatabaseEntity>>

    @Query("DELETE FROM notes_table WHERE id = :id")
    fun deleteNote(id:Int)

    @Update
    fun updateNote(noteDatabaseEntity: NoteDatabaseEntity)

}