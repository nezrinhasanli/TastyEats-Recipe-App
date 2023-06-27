package com.nezrin.tastyeats.presentation.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.nezrin.tastyeats.data.model.Meal
import com.nezrin.tastyeats.presentation.adapters.CategoryMealsAdapter
import com.nezrin.tastyeats.databinding.ActivityCategoryMealBinding
import com.nezrin.tastyeats.presentation.adapters.MealsAdapter
import com.nezrin.tastyeats.presentation.adapters.OnMealClickListener
import com.nezrin.tastyeats.presentation.adapters.PopularMealAdapter
import com.nezrin.tastyeats.presentation.view.fragments.HomeFragment
import com.nezrin.tastyeats.viewmodel.CategoryMealActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryMealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryMealBinding
    private val viewModel by viewModels<CategoryMealActivityViewModel>()

    //    private lateinit var categoryMealsAdapter: CategoryMealsAdapter
    private lateinit var mealsAdapter: MealsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mealsAdapter = MealsAdapter(object : OnMealClickListener {
            override fun onMealClick(meal: Meal) {
                val intent = Intent(this@CategoryMealActivity, MealActivity::class.java)
                intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
                intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
                intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
                startActivity(intent)
            }

        })

/*categoryMealsAdapter = CategoryMealsAdapter()*/
        binding.mealRecyclerview.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = mealsAdapter
        }

        viewModel.getMealByCategoryVM(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)
        viewModel.mealsLiveData.observe(this) {
            binding.tvCategoryCount.text = it.meals.size.toString()
            mealsAdapter.setAllMealsList(it.meals)
        }
    }
}
