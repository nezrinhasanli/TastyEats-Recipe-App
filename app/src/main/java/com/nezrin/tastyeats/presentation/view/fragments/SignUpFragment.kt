package com.nezrin.tastyeats.presentation.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nezrin.tastyeats.R
import com.nezrin.tastyeats.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.signupButton.setOnClickListener {
            val email = binding.signupEmail.text.toString()
            val password = binding.signupPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                Firebase.auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {

                    val hmap= hashMapOf<String,Any>()
                    hmap["email"]=email
                    hmap["password"]=password

                    Firebase.firestore.collection("Users")
                        .document(Firebase.auth.currentUser!!.uid)
                        .set(hmap)

                    Toast.makeText(requireContext(), "Signed Up", Toast.LENGTH_SHORT).show()
                }
                    .addOnFailureListener {

                        Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        }
        binding.backToLogin.setOnClickListener{
            findNavController().navigate(SignUpFragmentDirections.fromSignupToLogin())

        }

        return binding.root
    }

}