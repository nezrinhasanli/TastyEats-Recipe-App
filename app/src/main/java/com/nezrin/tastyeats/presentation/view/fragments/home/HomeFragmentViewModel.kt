package com.nezrin.tastyeats.presentation.view.fragments.home


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nezrin.tastyeats.data.model.Category
import com.nezrin.tastyeats.data.model.MealsByCategory
import com.nezrin.tastyeats.data.model.Meal
import com.nezrin.tastyeats.data.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class HomeFragmentViewModel @Inject constructor(private val repo:MealRepository) : ViewModel() {


    var randomMealLiveData = MutableLiveData<List<Meal>>()
    var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>()
    var categoryItemsLiveData = MutableLiveData<List<Category>>()
    var bottomSheetLiveData = MutableLiveData<Meal>()
    var searchMealLiveData = MutableLiveData<List<Meal>>()


    //init {
//    getRandomMealVM() //for rotate
//}
    fun getRandomMealVM() {

        viewModelScope.launch {
            val response = repo.getRandomMealRepo()
            if (response.isSuccessful) {
                val mealResponse = response.body()
                randomMealLiveData.postValue(mealResponse!!.meals)
            }
        }
    }

    fun getPopularItemsVM() {
        viewModelScope.launch {
            val response = repo.getPopularItems()
            if (response.isSuccessful) {
                val mealResponse = response.body()
                popularItemsLiveData.postValue(mealResponse!!.meals)
            }
        }
    }

    fun getCategoriesVM() {
        viewModelScope.launch {
            val response = repo.getCategories()
            if (response.isSuccessful) {
                val mealResponse = response.body()
                categoryItemsLiveData.postValue(mealResponse!!.categories)
            }
        }
    }

    fun getMealByID(id: String) {
        viewModelScope.launch {
            val response = repo.getMealByID(id)
            if (response.isSuccessful) {
                val mealResponse = response.body()
                bottomSheetLiveData.postValue(mealResponse!!.meals.first())
            }
        }
    }

    fun searchMeal(search: String) {
        viewModelScope.launch {
            val response = repo.searchMealByName(search)
            if (response.isSuccessful&&response.code()==200) {
                val mealResponse = response.body()
                searchMealLiveData.postValue(mealResponse?.meals)
            }
        }
    }

}