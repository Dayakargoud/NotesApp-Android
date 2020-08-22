package com.dayakar.simplenote.ui.notes

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.dayakar.ktorrestapi.network.SessionManager
import com.dayakar.simplenote.R
import com.dayakar.simplenote.databinding.NotesFragmentBinding
import com.dayakar.simplenote.work.RefreshDataWorker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

private const val TAG="Notes Fragment"

@AndroidEntryPoint
class NotesFragment : Fragment() {

    private val viewModel:NotesViewModel by viewModels()
    private lateinit var binding :NotesFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding=NotesFragmentBinding.inflate(inflater)
        val sessionManager=SessionManager(requireContext())
        val token=sessionManager.fetchAuthToken()
        if (token==null){
            findNavController().navigate(R.id.loginFragment)
        }else{
            loadNotesFromViewModel()

        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_notesFragment_to_addNotesFragment)
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.itemId==200){
           val note= viewModel.userNotes.value?.get(item.groupId)
            val noteItem= bundleOf("note" to note)
            findNavController().navigate(R.id.action_notesFragment_to_addNotesFragment,noteItem)
            return true
        }else if(item.itemId==300){
            val note= viewModel.userNotes.value?.get(item.groupId)
            note?.let { viewModel.deleteNotes(note)

            }
            Snackbar.make(binding.noteRoot,"Deleted",Snackbar.LENGTH_SHORT).show()
            return true

        }
        return super.onContextItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu,menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.syncNotes){
            syncNotes()
            Toast.makeText(context, "Syncing notes", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadNotesFromViewModel(){
        val notesAdapter=NotesAdapter()
        binding.notesRecyclerView.apply {
                                adapter=notesAdapter
                            }
        viewModel.userNotes.observe(viewLifecycleOwner, Observer {
            notesAdapter.submitList(it)
            if (it.isEmpty()){
                binding.noteInfo.visibility=View.VISIBLE
                binding.noteInfo.text="Click + to add Notes"
            }else{
                binding.noteInfo.text=""
                binding.noteInfo.visibility=View.GONE
            }

        })
    }

    private fun syncNotes(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val uploadWorkRequest = OneTimeWorkRequestBuilder<RefreshDataWorker>()
            .setConstraints(constraints)
            .build()

         WorkManager.getInstance(requireContext()).enqueue(uploadWorkRequest)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,  // LifecycleOwner
            callback
        )
    }



}