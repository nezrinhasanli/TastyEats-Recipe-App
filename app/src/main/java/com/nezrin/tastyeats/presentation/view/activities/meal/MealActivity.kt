package com.nezrin.tastyeats.presentation.view.activities.meal

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nezrin.tastyeats.R
import com.nezrin.tastyeats.data.model.Meal
import com.nezrin.tastyeats.databinding.ActivityMealBinding
import com.nezrin.tastyeats.presentation.view.fragments.home.HomeFragment
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.UUID

@AndroidEntryPoint

class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var youtubeLink: String
    private lateinit var currentMealKey: String
    private var mealToSave: Meal? = null
    private val viewModel by viewModels<MealActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getMealInfoFromIntent()
        setInfoInViews()
        checkFavoriteMeal()
        loadingCase()

        viewModel.getMealByIdVM(mealId)
        viewModel.mealDetailLiveData.observe(this) {
            onResponseCase()
            val meal = it[0]
            mealToSave = meal
            onYoutubeImgClick(meal)
            binding.tvCategoryInfo.text = "Category: ${meal.strCategory}"
            binding.tvAreaInfo.text = "Area: ${meal.strArea}"
            binding.tvContent.text = meal.strInstructions

            youtubeLink = meal.strYoutube
        }

        onFavoriteClick()
    }

    @SuppressLint("ShowToast")
    private fun onFavoriteClick() {
        binding.buttonFav.setOnClickListener {
            mealToSave?.let {
                lifecycleScope.launch {
                    addFavoriteToFirebase()

                }
            }
        }
    }

    private fun onYoutubeImgClick(it:Meal) {
        val videoUrl=it.strYoutube
        val videoId=videoUrl.substringAfter("v=")
            .substringBefore("&")

        binding.youtubePlayer.addYouTubePlayerListener(object :AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                youTubePlayer.cueVideo(videoId,0f)
            }
        })
    }

    private fun setInfoInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealInfoFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!

    }

    private fun loadingCase() {

        binding.buttonFav.visibility = View.INVISIBLE
        binding.tvCategoryInfo.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvAreaInfo.visibility = View.INVISIBLE
//        binding.imgYoutube.visibility = View.INVISIBLE

    }

    private fun onResponseCase() {
        binding.buttonFav.visibility = View.VISIBLE
        binding.tvCategoryInfo.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvAreaInfo.visibility = View.VISIBLE
//        binding.imgYoutube.visibility = View.VISIBLE

    }

    private fun addFavoriteToFirebase() {


        if (binding.buttonFav.tag == "isFavorite") {

            Toast.makeText(this, "Meal removed", Toast.LENGTH_SHORT).show()

            Firebase.firestore.collection("Favorite Meals")
                .document(Firebase.auth.currentUser!!.uid)
                .update(currentMealKey, FieldValue.delete())
            binding.buttonFav.setImageResource(R.drawable.ic_favorite_2)

        } else {
            val hMap = hashMapOf<String, Any>()
            val hMapKey = hashMapOf<Any, Any>()
            val randomKey = UUID.randomUUID().toString() //meal key'e verilen eded ve reqemler

            hMap["mealName"] = mealToSave!!.strMeal
            hMap["mealImg"] = mealToSave!!.strMealThumb
            hMap["mealId"] = mealToSave!!.idMeal
            hMap["mealKey"] = randomKey

            hMapKey[randomKey] = hMap //key'in icindeki melumatlar

            Firebase.firestore.collection("Favorite Meals")
                .document(Firebase.auth.currentUser!!.uid).set(hMapKey, SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(this, "Meal saved", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun checkFavoriteMeal() {

        val userId = Firebase.auth.currentUser?.uid

        if (userId != null) {
            Firebase.firestore.collection("Favorite Meals")
                .document(userId)
                .addSnapshotListener { value, error ->

                    if (value != null) {
                        if (value.exists()) {

                            val datas = value.data as HashMap<*, *>
                            for (data in datas) {
                                val value = data.value as HashMap<*, *>

                                val mealFirebaseId = value["mealId"] as String
                                val mealKey = value["mealKey"] as String

                                if (mealId.trim() == mealFirebaseId.trim()) {
                                    currentMealKey = mealKey
                                    binding.buttonFav.tag = "isFavorite"
                                    binding.buttonFav.setImageResource(R.drawable.ic_favorite)
                                    break
                                } else {
                                    binding.buttonFav.tag = "isNotFavorite"
                                    binding.buttonFav.setImageResource(R.drawable.ic_favorite_2)
                                }
                            }
                        }
                    }
                }
        }
    }

}