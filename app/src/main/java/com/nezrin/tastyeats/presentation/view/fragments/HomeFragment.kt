package com.nezrin.tastyeats.presentation.view.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.nezrin.tastyeats.R
import com.nezrin.tastyeats.presentation.adapters.CategoryAdapter
import com.nezrin.tastyeats.presentation.adapters.PopularMealAdapter
import com.nezrin.tastyeats.common.PreferenceHelper
import com.nezrin.tastyeats.common.PreferenceHelper.set
import com.nezrin.tastyeats.data.model.MealsByCategory
import com.nezrin.tastyeats.data.model.Meal
import com.nezrin.tastyeats.databinding.FragmentHomeBinding
import com.nezrin.tastyeats.presentation.view.activities.CategoryMealActivity
import com.nezrin.tastyeats.presentation.view.activities.MealActivity
import com.nezrin.tastyeats.viewmodel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeFragmentViewModel>()
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: PopularMealAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var sharedPreferences: SharedPreferences


    companion object {
        const val MEAL_ID = "com.nezrin.tastyeats.ui.fragments.idMeal"
        const val MEAL_NAME = "com.nezrin.tastyeats.ui.fragments.nameMeal"
        const val MEAL_THUMB = "com.nezrin.tastyeats.ui.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.nezrin.tastyeats.ui.fragments.categoryName"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        sharedPreferences= PreferenceHelper.getDefault(requireActivity())

        //popular meal adapter
        popularItemsAdapter = PopularMealAdapter()
        binding.recyclerViewMealsPopular.adapter = popularItemsAdapter
        binding.recyclerViewMealsPopular.setHasFixedSize(true)
        binding.recyclerViewMealsPopular.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        //category adapter
        categoryAdapter = CategoryAdapter()
        binding.recyclerViewCategory.adapter = categoryAdapter
        binding.recyclerViewCategory.setHasFixedSize(true)
        binding.recyclerViewCategory.layoutManager =
            GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)

//        viewModel = (activity as MainActivity).viewModel

        //random meal view model
        viewModel.getRandomMealVM()
        viewModel.randomMealLiveData.observe(viewLifecycleOwner) {
            Glide.with(this@HomeFragment)
                .load(it!![0].strMealThumb)
                .into(binding.imgRandomMeal)

            this.randomMeal = it[0]
        }
        onRandomMealClick()

        //popular item view model
        viewModel.getPopularItemsVM()
        viewModel.popularItemsLiveData.observe(viewLifecycleOwner) {

            popularItemsAdapter.setMeals(it as ArrayList<MealsByCategory>)
        }
        onPopularItemClick()

        //category view model
        viewModel.getCategoriesVM()
        viewModel.categoryItemsLiveData.observe(viewLifecycleOwner) {

            categoryAdapter.setCategoryList(it)
        }
        onCategoryClick()
        onPopularItemLongClick()
        onSearchItemClick()
        logoutClick()
        return binding.root
    }

    private fun onSearchItemClick() {
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.fromHomeToSearch)
        }
    }

    private fun onPopularItemLongClick() {
        popularItemsAdapter.onLongItemClick={
            val mealBottomSheetFragment= MealBottomSheetFragment.newInstance(it.idMeal)
            mealBottomSheetFragment.show(childFragmentManager,"Meal info")
        }
    }

    private fun onCategoryClick() {
        categoryAdapter.onItemClick = {
            val intent = Intent(activity, CategoryMealActivity::class.java)
            intent.putExtra(CATEGORY_NAME, it.strCategory)
            startActivity(intent)
        }

    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, it.idMeal)
            intent.putExtra(MEAL_NAME, it.strMeal)
            intent.putExtra(MEAL_THUMB, it.strMealThumb)

            startActivity(intent)
        }
    }


    private fun onRandomMealClick() {
        binding.randomMeal.setOnClickListener {

            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)

            startActivity(intent)
        }
    }
    private fun logoutClick(){
        binding.buttonLogout.setOnClickListener {
            sharedPreferences["email"]=null
            sharedPreferences["password"]=null
            findNavController().navigate(HomeFragmentDirections.fromHomeToLogin())

        }
    }
}