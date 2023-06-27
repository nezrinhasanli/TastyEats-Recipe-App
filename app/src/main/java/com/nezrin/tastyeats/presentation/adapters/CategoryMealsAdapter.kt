package com.nezrin.tastyeats.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nezrin.tastyeats.data.model.MealsByCategory
import com.nezrin.tastyeats.databinding.MealItemsBinding

class CategoryMealsAdapter:RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewHolder>() {

   private var mealList=ArrayList<MealsByCategory>()

    inner class CategoryMealsViewHolder(val binding:MealItemsBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewHolder {
        return CategoryMealsViewHolder(MealItemsBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CategoryMealsViewHolder, position: Int) {
        Glide.with(holder.itemView).load(mealList[position].strMealThumb).into(holder.binding.imgMeal)

        holder.binding.tvMealName.text=mealList[position].strMeal
    }

    override fun getItemCount(): Int {
       return mealList.size
    }
    fun setMealList(mealList:List<MealsByCategory>){

        this.mealList=mealList as ArrayList<MealsByCategory>
        notifyDataSetChanged()
    }

}