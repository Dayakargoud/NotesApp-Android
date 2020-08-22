package com.dayakar.simplenote.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dayakar.ktorrestapi.network.SessionManager
import com.dayakar.simplenote.R
import com.dayakar.simplenote.databinding.FragmentSignUpBinding
import com.dayakar.simplenote.network.NotesAPI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private val TAG="SignUpFragment"

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    @Inject lateinit var notesAPI: NotesAPI

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private lateinit var viewModel: SignUpViewModel
    private lateinit var binding:FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentSignUpBinding.inflate(inflater)
        binding.buttonSignup.setOnClickListener {
            val displayName=binding.usernameSignup.editText?.text.toString()
            val email=binding.emailSignUp.editText?.text.toString()
            val password=binding.passwordSignup.editText?.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                signUpUser(displayName,email,password)
                binding.buttonSignup.isEnabled=false
            }else{
                if (email.isEmpty()){
                    Toast.makeText(context, "Email can't be empty", Toast.LENGTH_SHORT).show()

                }
                if (password.isEmpty()){
                    Toast.makeText(context, "Password can't be empty", Toast.LENGTH_SHORT).show()

                }
            }
        }

        binding.loginSignUpText.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    private fun signUpUser(displayName:String,email: String, password: String) {
        binding.progressbar.visibility=View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val sessionManager = SessionManager(requireContext())
                val response = notesAPI.createUser(displayName,email, password)
                if (response.isSuccessful){
                    val user=  response.body()
                    user?.token?.let { sessionManager.saveAuthToken(it)
                    sessionManager.saveCurrentUserInfo(user)}

                    withContext(Dispatchers.Main){
                        binding.progressbar.visibility=View.INVISIBLE
                        findNavController().navigate(R.id.notesFragment)

                    }
                }else{
                    Log.d(TAG, "SignUp: ${response.body()}")
                    binding.buttonSignup.isEnabled=true
                }
            } catch (e: Throwable) {
                Log.d(TAG, "SignUp: ${e.message}")
                withContext(Dispatchers.Main){
                    binding.buttonSignup.isEnabled=true
                    binding.progressbar.visibility=View.INVISIBLE
                }


            }

        }

    }


}