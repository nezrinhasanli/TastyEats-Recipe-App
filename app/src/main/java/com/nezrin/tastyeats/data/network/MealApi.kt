package com.nezrin.tastyeats.data.network

import com.nezrin.tastyeats.data.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("random.php")
   suspend fun getRandomMeal():Response<MealList>

    @GET("lookup.php")
   suspend fun getMealById(@Query("i") id:String):Response<MealList>

    @GET("filter.php")
   suspend fun getPopularMeals(@Query("c") categoryName:String):Response<MealsByCategoryList>

    @GET("categories.php")
   suspend fun getCategories(): Response<CategoryList>

    @GET("filter.php")
   suspend fun getMealsByCategory(@Query("c") categoryName:String):Response<MealList>

    @GET("search.php")
   suspend fun searchMealByName(@Query("s") search:String):Response<MealList>

}