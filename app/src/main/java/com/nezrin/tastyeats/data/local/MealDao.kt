package com.nezrin.tastyeats.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nezrin.tastyeats.data.model.Meal

@Dao
interface MealDao {

    @Insert
   suspend fun insertMeal(meal: Meal)

   @Update
   suspend fun updateMeal(meal: Meal)

    @Delete
   suspend fun deleteMeal(meal:Meal)

    @Query("SELECT * FROM mealInformation")
    fun getAllMeals(): LiveData<List<Meal>>

    @Query("SELECT * FROM mealInformation WHERE idMeal = :mealId")
   suspend fun getMealById(mealId: String): Meal?
}