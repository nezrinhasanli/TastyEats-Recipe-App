package com.nezrin.tastyeats.presentation.view.fragments.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.nezrin.tastyeats.presentation.adapters.MealsAdapter
import com.nezrin.tastyeats.data.model.Meal
import com.nezrin.tastyeats.databinding.FragmentSearchBinding
import com.nezrin.tastyeats.presentation.adapters.OnMealClickListener
import com.nezrin.tastyeats.presentation.view.activities.meal.MealActivity
import com.nezrin.tastyeats.presentation.view.fragments.home.HomeFragment
import com.nezrin.tastyeats.presentation.view.fragments.home.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModels<HomeFragmentViewModel>()
    private lateinit var searchAdapter: MealsAdapter

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        //adapter
        searchAdapter = MealsAdapter(object: OnMealClickListener{
            override fun onMealClick(meal: Meal) {
                val intent = Intent(requireContext(), MealActivity::class.java)
                intent.putExtra(HomeFragment.MEAL_ID,meal.idMeal)
                intent.putExtra(HomeFragment.MEAL_NAME,meal.strMeal)
                intent.putExtra(HomeFragment.MEAL_THUMB,meal.strMealThumb)
                startActivity(intent)
            }

        })
        binding.rvSearch.adapter=searchAdapter
        binding.rvSearch.layoutManager =
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)


        binding.imgSearch.setOnClickListener { searchMeal()}

            viewModel.searchMealLiveData.observe(viewLifecycleOwner){
                if (!it.isNullOrEmpty()) {
                searchAdapter.setAllMealsList(it)
                }else {
                    Toast.makeText(requireContext(), "Meal does not exist", Toast.LENGTH_SHORT).show()
                }
            }

        var searchJob: Job?=null
        binding.edSearch.addTextChangedListener {
            searchJob?.cancel()
            searchJob=lifecycleScope.launch {
                delay(500)
                viewModel.searchMeal(it.toString())
            }
        }


        return binding.root
    }

    private fun searchMeal() {
        val search = binding.edSearch.text.toString()
        if (search.isNotEmpty()) {
            viewModel.searchMeal(search)
        }
    }

}