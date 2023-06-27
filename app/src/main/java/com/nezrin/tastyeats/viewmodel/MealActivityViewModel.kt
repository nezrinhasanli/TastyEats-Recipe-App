package com.nezrin.tastyeats.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nezrin.tastyeats.data.local.MealDao
import com.nezrin.tastyeats.data.model.Meal
import com.nezrin.tastyeats.data.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel

class MealActivityViewModel @Inject constructor(
    private val mealDao: MealDao,
    private val repo: MealRepository
) : ViewModel() {

    var mealDetailLiveData = MutableLiveData<List<Meal>>()


    fun getMealByIdVM(id: String) {
        viewModelScope.launch {
            val response = repo.getMealByID(id)
            if (response.isSuccessful) {
                val mealResponse = response.body()
                mealDetailLiveData.postValue(mealResponse!!.meals)
            }
        }
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDao.insertMeal(meal)
        }
    }

   suspend fun isMealExists(meal: Meal): Boolean {
       //Additionally, the withContext(Dispatchers.IO) block in isMealExists()
       // ensures that the database operation is executed on a separate IO thread,
       // preventing it from blocking the main thread.
        return withContext(Dispatchers.IO) {
            mealDao.getMealById(meal.idMeal) != null
        }
    }
}