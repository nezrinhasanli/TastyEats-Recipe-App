package com.nezrin.tastyeats.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.nezrin.tastyeats.data.model.Meal
import com.nezrin.tastyeats.databinding.MealItemsBinding

class MealsAdapter(private  var onMealClickListener: OnMealClickListener
) : RecyclerView.Adapter<MealsAdapter.FavoritesMealViewHolder>() {
    private var allMeals: List<Meal> = ArrayList()

    inner class FavoritesMealViewHolder(val binding: MealItemsBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesMealViewHolder {
        return FavoritesMealViewHolder(
            MealItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoritesMealViewHolder, position: Int) {

        Glide.with(holder.itemView).load(allMeals[position].strMealThumb)
            .into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = allMeals[position].strMeal

        holder.binding.imgMeal.setOnClickListener {
            onMealClickListener.onMealClick(allMeals[position])
        }
    }

    override fun getItemCount(): Int {

        return allMeals.size
    }

    fun setAllMealsList(favoriteMeals: List<Meal>) {
        this.allMeals = favoriteMeals
        notifyDataSetChanged()
    }


    fun getMealByPosition(position: Int): Meal {
        return allMeals[position]
    }

}
interface OnMealClickListener {
    fun onMealClick(meal: Meal)
}