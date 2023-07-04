package com.nezrin.tastyeats.presentation.view.fragments.favorites

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nezrin.tastyeats.data.model.Meal

class FavoriteFragmentViewModel:ViewModel() {

    var favMealLiveData = MutableLiveData<List<Meal>>()

    init {
        getFavoriteInfo()
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
                        var favoritesList=ArrayList<Meal>()
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
                            favoritesList.add(favoriteMeal)
                        }
                        favMealLiveData.postValue(favoritesList)

                    } catch (e: java.lang.Exception) {

                    }

                }
            }
    }
}