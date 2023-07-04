package com.nezrin.tastyeats.presentation.view.fragments.splash_screen

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nezrin.tastyeats.R
import com.nezrin.tastyeats.common.PreferenceHelper


class SplashScreenFragment : Fragment() {
//   private lateinit var binding:FragmentSplashScreenBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        checkLoginSession()


        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    private fun checkLoginSession(){
        sharedPreferences=PreferenceHelper.getDefault(requireActivity())

        val email=sharedPreferences.getString("email",null)
            val password=sharedPreferences.getString("password",null)

        if (email !=null && password !=null){
            Firebase.auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                Handler(Looper.getMainLooper()).postDelayed({
                    activity?.let {
                        findNavController().navigate(SplashScreenFragmentDirections.fromSplashToHome())
                    }
                },900)
            }.addOnFailureListener {
                Toast.makeText(requireActivity(), it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        } else{
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(SplashScreenFragmentDirections.fromSplashToLogin())

            },2000)
        }
    }

}