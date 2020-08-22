package com.dayakar.simplenote.ui.notes

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dayakar.simplenote.model.Note
import com.dayakar.simplenote.repository.MainRepository
import kotlinx.coroutines.launch
import java.io.IOException


class NotesViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : ViewModel() {

    val userNotes=mainRepository.userNotes

    init {
        loadNotes()
    }
    private fun loadNotes(){
        viewModelScope.launch {
            try {
                mainRepository.refreshNotes()
            }catch (e:IOException){

            }
        }
    }

    fun deleteNotes(note: Note){
          viewModelScope.launch {
              try {
                  mainRepository.deleteNote(note)
              }catch (e:Exception){

              }
          }

    }


}