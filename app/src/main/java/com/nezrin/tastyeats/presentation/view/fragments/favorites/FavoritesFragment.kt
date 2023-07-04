package com.nezrin.tastyeats.presentation.view.fragments.favorites

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nezrin.tastyeats.presentation.adapters.MealsAdapter
import com.nezrin.tastyeats.data.model.Meal
import com.nezrin.tastyeats.databinding.FragmentFavoritesBinding
import com.nezrin.tastyeats.presentation.adapters.OnMealClickListener
import com.nezrin.tastyeats.presentation.view.activities.meal.MealActivity
import com.nezrin.tastyeats.presentation.view.fragments.home.HomeFragment.Companion.MEAL_ID
import com.nezrin.tastyeats.presentation.view.fragments.home.HomeFragment.Companion.MEAL_NAME
import com.nezrin.tastyeats.presentation.view.fragments.home.HomeFragment.Companion.MEAL_THUMB
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var favoritesAdapter: MealsAdapter
    private val viewModel by viewModels<FavoriteFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        //favorite meal adapter
        favoritesAdapter = MealsAdapter(object : OnMealClickListener {
            override fun onMealClick(meal: Meal) {
                val intent = Intent(requireContext(), MealActivity::class.java)
                intent.putExtra(MEAL_ID, meal.idMeal)
                intent.putExtra(MEAL_NAME, meal.strMeal)
                intent.putExtra(MEAL_THUMB, meal.strMealThumb)
                startActivity(intent)
            }

        })
        binding.rvFavorites.adapter = favoritesAdapter
        binding.rvFavorites.setHasFixedSize(true)
        binding.rvFavorites.layoutManager =
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)

        viewModel.favMealLiveData.observe(viewLifecycleOwner){
            favoritesAdapter.setAllMealsList(it)
        }

        //delete items from favorites
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val favMeal = favoritesAdapter.getMealByPosition(position)
                val currentKey = favMeal.mealKey
                Firebase.firestore.collection("Favorite Meals")
                    .document(Firebase.auth.currentUser!!.uid).update(currentKey,FieldValue.delete())
                showDeleteSnackBar(favMeal)
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorites)


        return binding.root
    }

    private fun showDeleteSnackBar(meal: Meal) {
        Snackbar.make(requireView(), "Meal Deleted", Snackbar.LENGTH_LONG).apply {
            setAction(
                "Undo", View.OnClickListener {

                    val hMap= hashMapOf<String,Any>()
                    val hMapKey= hashMapOf<Any,Any>()
                    val randomKey= meal.mealKey

                    hMap["mealName"]= meal.strMeal
                    hMap["mealImg"]=meal.strMealThumb
                    hMap["mealId"]=meal.idMeal
                    hMap["mealKey"]=randomKey
                    hMapKey[randomKey]=hMap
                    Firebase.firestore.collection("Favorite Meals").document(Firebase.auth.currentUser!!.uid)
                        .set(hMapKey, SetOptions.merge())

                }).show()
        }
    }

}