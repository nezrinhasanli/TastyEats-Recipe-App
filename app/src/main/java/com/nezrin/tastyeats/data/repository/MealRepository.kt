package com.nezrin.tastyeats.data.repository

import com.nezrin.tastyeats.data.model.*
import com.nezrin.tastyeats.data.network.MealApi
import retrofit2.Response
import javax.inject.Inject

class MealRepository @Inject constructor(private val api:MealApi) {

    suspend fun getRandomMeal(): Response<MealList> {
        return api.getRandomMeal()
    }
    suspend fun getPopularMeals(): Response<MealsByCategoryList> {
        return api.getPopularMeals("SeaFood")
    }
    suspend fun getCategories(): Response<CategoryList> {
        return api.getCategories()
    }

    suspend fun getMealByID(id:String): Response<MealList> {
        return api.getMealById(id)
    }
    suspend fun searchMealByName(search:String): Response<MealList> {
        return api.searchMealByName(search)
    }

    suspend fun getMealsByCategory(categoryName:String):Response<MealList>{
        return api.getMealsByCategory(categoryName)
    }
}