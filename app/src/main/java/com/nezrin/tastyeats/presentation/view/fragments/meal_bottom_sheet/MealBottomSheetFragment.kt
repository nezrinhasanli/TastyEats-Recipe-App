package com.nezrin.tastyeats.presentation.view.fragments.meal_bottom_sheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nezrin.tastyeats.databinding.FragmentMealBottomSheetBinding
import com.nezrin.tastyeats.presentation.view.activities.meal.MealActivity
import com.nezrin.tastyeats.presentation.view.fragments.home.HomeFragment
import com.nezrin.tastyeats.presentation.view.fragments.home.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val MEAL_ID="param1"
@AndroidEntryPoint

class MealBottomSheetFragment : BottomSheetDialogFragment() {

    private var mealId:String?=null
    private lateinit var binding:FragmentMealBottomSheetBinding
    private val viewModel by viewModels<HomeFragmentViewModel>()
    private var mealName:String?=null
    private var mealThumb:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId=it.getString(MEAL_ID)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentMealBottomSheetBinding.inflate(inflater,container,false)

//        viewModel = (activity as MainActivity).viewModel

        viewModel.getMealByID(mealId!!)
        viewModel.bottomSheetLiveData.observe(viewLifecycleOwner){

            Glide.with(this).load(it.strMealThumb).into(binding.imgCategoryBottom)
            binding.tvMealCountry.text=it.strArea
            binding.tvMealCategory.text=it.strCategory
            binding.tvMealNameInBtmsheet.text=it.strMeal

            mealName=it.strMeal
            mealThumb=it.strMealThumb

        }

        onBottomSheetDialogClick()

        return binding.root
    }

    private fun onBottomSheetDialogClick() {
        binding.bottomSheet.setOnClickListener{

            if (mealName!=null && mealThumb!=null){
                val intent=Intent(activity, MealActivity::class.java)

                intent.apply {
                    putExtra(HomeFragment.MEAL_ID,mealId)
                    putExtra(HomeFragment.MEAL_NAME,mealName)
                    putExtra(HomeFragment.MEAL_THUMB,mealThumb)
                }
                startActivity(intent)
            }
        }
    }

    companion object{
        @JvmStatic
        fun newInstance(param1:String) =
            MealBottomSheetFragment().apply {
                arguments=Bundle().apply {
                    putString(MEAL_ID,param1)
                }
            }

    }

}