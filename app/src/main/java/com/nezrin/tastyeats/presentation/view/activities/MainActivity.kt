package com.nezrin.tastyeats.presentation.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nezrin.tastyeats.R
import com.nezrin.tastyeats.databinding.ActivityMainBinding
import com.nezrin.tastyeats.databinding.ActivityMealBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setup bottom nav
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNav,navHostFragment.navController)

        navHostFragment.navController.addOnDestinationChangedListener{_,destination,_->
            when(destination.id){
                R.id.splashScreenFragment-> binding.bottomNav.visibility=GONE
                R.id.loginFragment -> binding.bottomNav.visibility=GONE
                R.id.signUpFragment -> binding.bottomNav.visibility=GONE

                else -> {
                    binding.bottomNav.visibility= VISIBLE
                }

            }

        }

    }
}