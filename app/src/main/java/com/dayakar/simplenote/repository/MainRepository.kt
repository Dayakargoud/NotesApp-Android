package com.dayakar.simplenote.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.dayakar.simplenote.database.DatabaseMapper
import com.dayakar.simplenote.database.NoteDatabase
import com.dayakar.simplenote.database.NoteDatabaseEntity
import com.dayakar.simplenote.model.Note
import com.dayakar.simplenote.model.NoteSyncModel
import com.dayakar.simplenote.network.NoteNetworkEntity
import com.dayakar.simplenote.network.NotesAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

private const val TAG="Repository"
class MainRepository @Inject constructor(
    private val database:NoteDatabase,
    private val notesAPI: NotesAPI) {


    val userNotes:LiveData<List<Note>> = Transformations.map(database.notesDao().getAllNotes()) {
        DatabaseMapper().mapFromEntityList(it)}

    suspend fun refreshNotes(){
      withContext(Dispatchers.IO) {
          val notesList = notesAPI.getmyNotes().body()
          updateDatabase(notesList)
      }

   }

    private suspend fun updateDatabase(notesList:ArrayList<NoteNetworkEntity>?){
             val list=notesList?.asDatabaseModel(notesList)
             list?.forEach {
                 database.notesDao().insert(it)
             }
         }

    suspend fun addNotes(note:Note){
        withContext(Dispatchers.IO) {
            try {
                val itemNote=DatabaseMapper().mapToEntity(note)
                database.notesDao().insert(itemNote)
                val response=notesAPI.addNote(convertNetworkModel(note))

                if (response.isSuccessful){
                    val addedItem=response.body()
                    addedItem?.let {
                        val dataItem=convertDatabaseModel(it)
                        Log.d(TAG, "added Network response Note: $dataItem")
                        database.notesDao().updateNote(dataItem)
                    }

                }else{
                    Log.d(TAG, "update error Response=${response.errorBody()} ")

                }

            }catch (e: IOException){
                Log.d("Repository", "addNotes: ${e.message}")
            }
        }
    }

    suspend fun deleteNote(note:Note){
        withContext(Dispatchers.IO) {
            try {
                database.notesDao().deleteNote(note.id)
            val response= notesAPI.deleteNote(note.id)
                Log.d(TAG, "delete Response=$response: ")
            }catch (e: IOException){
                Log.d("Repository", "delete: ${e.message}")
            }
        }
    }

    suspend fun updateNote(note:Note){
        withContext(Dispatchers.IO) {
            try {
                val itemNote=DatabaseMapper().mapToEntity(note)
                database.notesDao().updateNote(itemNote)
                val response= notesAPI.updateNote(convertNetworkModel(note))
                if (response.isSuccessful){
                    val responseItem=response.body()
                    responseItem?.let {
                        val dataItem=convertDatabaseModel(responseItem)
                        Log.d(TAG, "update Network response Note: $dataItem")

                        database.notesDao().updateNote(dataItem)
                    }
                }else{
                    Log.d(TAG, "update error Response=${response.errorBody()} ")

                }
            }catch (e: IOException){

                Log.d("Repository", "update: ${e.message}")
            }
        }
    }

    suspend fun syncNotes(){
        withContext(Dispatchers.IO) {
            delay(2000)
                val localNotesList=   userNotes.value
                val uploadList= localNotesList?.asNetworkModel(localNotesList)

            try {
                uploadList?.let {
                  val syncResponse=  notesAPI.syncNotes(uploadList)
                    if (syncResponse.isSuccessful){
                        Log.d("Repository", "syncNotes Body= ${syncResponse.body()} ")
                        val updatedNotes=syncResponse.body()
                        updateDatabase(updatedNotes)


                    }else{
                        Log.d("Repository", "syncNotes data server:= $syncResponse ")

                    }
                }

            }catch (e:Exception){
                Log.d("Repository", "syncNotes exception=: ${e.message}")
            }

        }
    }

    private suspend fun List<NoteNetworkEntity>.asDatabaseModel(entities:List<NoteNetworkEntity>):List<NoteDatabaseEntity>{
        return entities.map {
            convertDatabaseModel(it)
        }

    }
    private fun convertDatabaseModel(noteNetworkEntity: NoteNetworkEntity):NoteDatabaseEntity{
        return NoteDatabaseEntity(
            id=noteNetworkEntity.id,
            userId = noteNetworkEntity.userId,
            title = noteNetworkEntity.title,
            note = noteNetworkEntity.note,
            updatetime = noteNetworkEntity.updatetime,
            isSynced = true
        )
    }

    private fun List<Note>.asNetworkModel(entities:List<Note>):List<NoteSyncModel>{
        return entities.map {convertNetworkModel(it)
        }
    }
    private fun convertNetworkModel(model: Note):NoteSyncModel{
        return NoteSyncModel(
            id=model.id,
            userId = model.userId,
            title = model.title,
            note = model.note,
            updatetime = model.updatetime)
    }


}

