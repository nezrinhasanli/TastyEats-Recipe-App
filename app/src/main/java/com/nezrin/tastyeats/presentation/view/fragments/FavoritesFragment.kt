package com.nezrin.tastyeats.presentation.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.nezrin.tastyeats.data.model.MealList
import com.nezrin.tastyeats.databinding.FragmentFavoritesBinding
import com.nezrin.tastyeats.presentation.adapters.OnMealClickListener
import com.nezrin.tastyeats.presentation.view.activities.MealActivity
import com.nezrin.tastyeats.presentation.view.fragments.HomeFragment.Companion.MEAL_ID
import com.nezrin.tastyeats.presentation.view.fragments.HomeFragment.Companion.MEAL_NAME
import com.nezrin.tastyeats.presentation.view.fragments.HomeFragment.Companion.MEAL_THUMB
import com.nezrin.tastyeats.viewmodel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@AndroidEntryPoint

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
//    private val viewModel by viewModels<HomeFragmentViewModel>()
    private lateinit var favoritesAdapter: MealsAdapter
    private lateinit var arrayList: ArrayList<Meal>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        arrayList = ArrayList()

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


        getFavoriteInfo()

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

    private fun getFavoriteInfo() {
        Firebase.firestore.collection("Favorite Meals").document(Firebase.auth.currentUser!!.uid)
            .addSnapshotListener { value, error ->

                if (error != null) {
                    Log.e("TAG", "getFavoriteInfo: ${error.localizedMessage}", )

                    return@addSnapshotListener
                }
                if (value != null) {
                    try {
                        arrayList.clear()
                        val datas = value.data as HashMap<*,*>
                        for (data in datas) {
                            val value = data.value as HashMap<*,*>
                            val mealName = value["mealName"] as String
                            val mealImg = value["mealImg"] as String
                            val mealId = value["mealId"] as String
                            val mealKey = value["mealKey"] as String
                            val favoriteMeal = Meal(
                                idMeal = mealId,
                                strMeal = mealName,
                                strMealThumb = mealImg,
                                mealKey = mealKey
                            )
//
//                            if (id==mealId){
//                                // fab butonun seklin deyisdir
//                                return@addSnapshotListener
//                            }
                            arrayList.add(favoriteMeal)

                        }
                        Log.e("TAG", "getFavoriteInfo: ${arrayList.toString()}", )

                        favoritesAdapter.setAllMealsList(arrayList)
                    } catch (e: java.lang.Exception) {

                    }

                }
            }
    }
}