package com.nezrin.tastyeats.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nezrin.tastyeats.data.model.MealsByCategory
import com.nezrin.tastyeats.databinding.PopularItemsBinding

class PopularMealAdapter():RecyclerView.Adapter<PopularMealAdapter.PopularMealViewHolder>(){

    private var mealsList=ArrayList<MealsByCategory>()
    lateinit var onItemClick:((MealsByCategory)->Unit)
    var onLongItemClick:((MealsByCategory)->Unit)?=null
    class PopularMealViewHolder(val binding: PopularItemsBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {

        return PopularMealViewHolder(PopularItemsBinding
            .inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.imgPopularMeal)

        holder.itemView.setOnClickListener{
            onItemClick.invoke(mealsList[position])
        }

        holder.itemView.setOnLongClickListener {

            onLongItemClick?.invoke(mealsList[position])
            true
        }
    }

    override fun getItemCount(): Int {
       return mealsList.size
    }

    fun setMeals(mealList: ArrayList<MealsByCategory>){
        this.mealsList=mealList
        notifyDataSetChanged()
    }
}




