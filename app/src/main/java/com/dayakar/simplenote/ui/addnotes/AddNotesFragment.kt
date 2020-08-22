package com.dayakar.simplenote.ui.addnotes

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dayakar.ktorrestapi.network.SessionManager
import com.dayakar.simplenote.R
import com.dayakar.simplenote.databinding.AddNotesFragmentBinding
import com.dayakar.simplenote.model.Note
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates
import kotlin.random.Random

@AndroidEntryPoint
class AddNotesFragment : Fragment() {

    private val viewModel:AddNotesViewModel by viewModels()
    private lateinit var binding:AddNotesFragmentBinding
     private var noteItem:Note?=null
    private var randomNoteId by Delegates.notNull<Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding=AddNotesFragmentBinding.inflate(inflater)
        randomNoteId=getRandomId()

        arguments?.let {
            noteItem=it.getParcelable("note")
        }

        noteItem?.let {
            binding.noteTitle.setText(it.title)
            binding.noteContent.setText(it.note)
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu,menu)
    }

    private fun getRandomId():Int{
        return Random.nextInt(6000,Int.MAX_VALUE)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId==R.id.save){
            if (noteItem!=null){
                updateInDatabase()
                Toast.makeText(context, "Note updated", Toast.LENGTH_SHORT).show()
            }else{
                addNoteToDatabase()
                Toast.makeText(context, "Note saved", Toast.LENGTH_SHORT).show()

            }

        }else if (item.itemId==R.id.cancel){
            findNavController().navigateUp()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addNoteToDatabase(){
        val title=binding.noteTitle.text.toString()
        val content=binding.noteContent.text.toString()

        if (title.isNotEmpty()){
            val id= randomNoteId
            val sessionManager=SessionManager(requireContext())
           val  currentUser=sessionManager.getCurrentUser()
            Log.d("AddNotes", "addNoteToDatabase: $currentUser")
            currentUser?.let {
                val note=Note(id =id,userId = it.id,title = title,note = content,updatetime = System.currentTimeMillis().toString(),isSynced = false)
                viewModel.addNotes(note)
            }


        }
    }

    private fun updateInDatabase(){
        val title=binding.noteTitle.text.toString()
        val content=binding.noteContent.text.toString()
        if (title.isNotEmpty()){
            noteItem?.let {
                Log.d("Add Fragment", "updateInDatabase: Update Note")
                    val note=Note(it.id,it.userId,title = title,note = content,updatetime = System.currentTimeMillis().toString(),isSynced = false)
                   viewModel.updateNotes(note)

                }

            }
        }
    }


