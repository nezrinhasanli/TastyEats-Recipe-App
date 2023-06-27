package com.nezrin.tastyeats.presentation.view.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nezrin.tastyeats.R
import com.nezrin.tastyeats.common.PreferenceHelper
import com.nezrin.tastyeats.common.PreferenceHelper.set
import com.nezrin.tastyeats.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding:FragmentLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(inflater, container, false)

        sharedPreferences=PreferenceHelper.
        getDefault(requireActivity())

        binding.signupRedirectText.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.fromLoginToSignup())
        }
        binding.loginButton.setOnClickListener {

            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                Firebase.auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {

                    sharedPreferences["email"]=email
                    sharedPreferences["password"]=password

                    findNavController().navigate(LoginFragmentDirections.fromLoginToHome())
                }
                    .addOnFailureListener {

                        Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        }

            return binding.root
        }

    }