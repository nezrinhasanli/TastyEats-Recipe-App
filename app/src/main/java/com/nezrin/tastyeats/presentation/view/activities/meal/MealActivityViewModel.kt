package com.nezrin.tastyeats.presentation.view.activities.meal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nezrin.tastyeats.data.model.Meal
import com.nezrin.tastyeats.data.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class MealActivityViewModel @Inject constructor(private val repo: MealRepository) : ViewModel() {

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

}