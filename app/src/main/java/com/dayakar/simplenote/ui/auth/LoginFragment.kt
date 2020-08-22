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
import com.dayakar.simplenote.databinding.FragmentLoginBinding
import com.dayakar.simplenote.network.NotesAPI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG="LoginFragment"
@AndroidEntryPoint
class LoginFragment : Fragment() {

    @Inject lateinit var notesAPI: NotesAPI

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding:FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding=FragmentLoginBinding.inflate(inflater)

        binding.loginButton.setOnClickListener {
            val password=binding.loginPassword.editText?.text.toString()
            val email=  binding.loginEmail.editText?.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                loginUser(email,password)
                binding.loginButton.isEnabled=false
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

            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        return  binding.root
    }

    private fun loginUser(email:String,password:String){
        binding.progressbar.visibility=View.VISIBLE
        val sessionManager=SessionManager(requireContext())

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = notesAPI.loginUser(email, password)
                if (response.isSuccessful){
                  val user=  response.body()
                    user?.token?.let { sessionManager.saveAuthToken(it)
                    sessionManager.saveCurrentUserInfo(user)}

                    withContext(Dispatchers.Main){
                        Toast.makeText(context, "${user}", Toast.LENGTH_SHORT).show()
                        binding.progressbar.visibility=View.INVISIBLE
                        findNavController().navigate(R.id.notesFragment)

                    }
                }else{
                    withContext(Dispatchers.Main){
                        binding.loginButton.isEnabled=true
                        binding.progressbar.visibility=View.INVISIBLE

                    }

                }
            } catch (e: Throwable) {
                Log.d(TAG, "login: ${e.message}")
                withContext(Dispatchers.Main){
                    binding.loginButton.isEnabled=true
                    binding.progressbar.visibility=View.INVISIBLE

                }



            }

        }





    }




}