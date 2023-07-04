package com.nezrin.tastyeats.presentation.view.fragments.categories

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.nezrin.tastyeats.presentation.adapters.CategoryAdapter
import com.nezrin.tastyeats.databinding.FragmentCategoriesBinding
import com.nezrin.tastyeats.presentation.view.activities.category_meal.CategoryMealActivity
import com.nezrin.tastyeats.presentation.view.fragments.home.HomeFragment.Companion.CATEGORY_NAME
import com.nezrin.tastyeats.presentation.view.fragments.home.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var categoriesAdapter: CategoryAdapter
    private val viewModel by viewModels<HomeFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)


        //categories adapter
        categoriesAdapter = CategoryAdapter()
        binding.rvCategories.adapter = categoriesAdapter
        binding.rvCategories.layoutManager =
            GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)

        viewModel.getCategoriesVM()
        viewModel.categoryItemsLiveData.observe(viewLifecycleOwner) {

            categoriesAdapter.setCategoryList(it)
        }

        onCategoryClick()

        return binding.root
    }

    private fun onCategoryClick() {

        categoriesAdapter.onItemClick = {
            val intent = Intent(activity, CategoryMealActivity::class.java)
            intent.putExtra(CATEGORY_NAME, it.strCategory)
            startActivity(intent)
        }
    }

}