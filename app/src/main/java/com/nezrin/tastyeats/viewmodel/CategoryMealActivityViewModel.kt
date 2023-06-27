package com.nezrin.tastyeats.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nezrin.tastyeats.data.model.*
import com.nezrin.tastyeats.data.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryMealActivityViewModel @Inject constructor(private val repo: MealRepository):ViewModel() {

    var mealsLiveData=MutableLiveData<MealList>()

    fun getMealByCategoryVM(categoryName:String){
        viewModelScope.launch {
            val response = repo.getMealsByCategory(categoryName)
            if (response.isSuccessful) {
                val mealResponse = response.body()
                mealsLiveData.postValue(mealResponse!!)
            }
        }
    }
}