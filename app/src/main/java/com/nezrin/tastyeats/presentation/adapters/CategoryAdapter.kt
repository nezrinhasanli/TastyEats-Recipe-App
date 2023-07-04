package com.nezrin.tastyeats.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nezrin.tastyeats.data.model.Category
import com.nezrin.tastyeats.databinding.CategoryItemsBinding

class CategoryAdapter:RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var categoryList=ArrayList<Category>()
    var onItemClick:((Category)->Unit)?=null

    inner class CategoryViewHolder(val binding:CategoryItemsBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        return CategoryViewHolder(CategoryItemsBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(categoryList[position].strCategoryThumb)
            .into(holder.binding.imgCategory)

        holder.binding.tvCategoryName.text=categoryList[position].strCategory
        holder.itemView.setOnClickListener {
            onItemClick!!.invoke(categoryList[position])

        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun setCategoryList(categoryList: List<Category>){
        this.categoryList= categoryList as ArrayList<Category>
        notifyDataSetChanged()
    }

}
