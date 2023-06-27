package com.nezrin.tastyeats.data.local

import android.content.Context
import androidx.room.*
import com.nezrin.tastyeats.data.model.Meal

@Database(entities = [Meal::class], version = 2, exportSchema = false)
@TypeConverters(MealTypeConverter::class)
abstract class MealDatabase:RoomDatabase() {
    abstract fun mealDao():MealDao


}