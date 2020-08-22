package com.dayakar.simplenote.ui.addnotes

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dayakar.simplenote.model.Note
import com.dayakar.simplenote.repository.MainRepository
import kotlinx.coroutines.launch
import java.io.IOException

class AddNotesViewModel  @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    @Assisted private val savedStateHandle: SavedStateHandle): ViewModel() {


    fun addNotes(note: Note){
        viewModelScope.launch {
            try {
                mainRepository.addNotes(note)
            }catch (e: IOException){

            }
        }
    }

    fun updateNotes(note:Note) {
        viewModelScope.launch {
            try {
                 mainRepository.updateNote(note)
            } catch (e: IOException) {

            }
        }
    }

}