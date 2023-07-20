package com.nezrin.tastyeats.presentation.view.fragments.profile


import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.nezrin.tastyeats.common.PreferenceHelper
import com.nezrin.tastyeats.common.PreferenceHelper.set
import com.nezrin.tastyeats.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentProfileBinding.inflate(inflater, container, false)

        sharedPreferences = PreferenceHelper.getDefault(requireActivity())

        binding.logoutButton.setOnClickListener {

            sharedPreferences["email"] = null
            sharedPreferences["password"] = null
            findNavController().navigate(ProfileFragmentDirections.fromProfileToLogin())
            Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
        }



        binding.editProfile.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.fromProfileToEdit())
        }

        getUserInfo()
        return binding.root
    }

    private fun getUserInfo(){
        Firebase.firestore.collection("Users").document(Firebase.auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { value ->
                if (value!=null){
                    val username=value.get("username") as String
                    binding.username.setText(username)
                    val email=value.get("email") as String
                    binding.email.setText(email)
                    val imageUrl=value.get("imageUrl") as String
                    Glide.with(requireContext()).load(imageUrl).into(binding.profileImage)
                }
            }
    }
}
