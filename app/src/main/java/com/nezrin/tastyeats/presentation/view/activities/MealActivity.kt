package com.nezrin.tastyeats.presentation.view.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nezrin.tastyeats.R
import com.nezrin.tastyeats.data.model.Meal
import com.nezrin.tastyeats.databinding.ActivityMealBinding
import com.nezrin.tastyeats.presentation.view.fragments.HomeFragment
import com.nezrin.tastyeats.viewmodel.MealActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.UUID

@AndroidEntryPoint

class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding
    private lateinit var mealId:String
    private lateinit var mealName:String
    private lateinit var mealThumb:String
    private lateinit var youtubeLink:String
    private var mealToSave:Meal?=null
    private val viewModel by viewModels<MealActivityViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)


        getMealInfoFromIntent()
        setInfoInViews()


        loadingCase()

        viewModel.getMealByIdVM(mealId)
        viewModel.mealDetailLiveData.observe(this){
            onResponseCase()
            val meal=it[0]
            mealToSave=meal

            binding.tvCategoryInfo.text="Category: ${meal.strCategory}"
            binding.tvAreaInfo.text="Area: ${meal.strArea}"
            binding.tvContent.text=meal.strInstructions

            youtubeLink=meal.strYoutube
        }

        onYoutubeImgClick()
        onFavoriteClick()
    }

    private fun onFavoriteClick() {
        binding.buttonFav.setOnClickListener {

            mealToSave?.let {
                lifecycleScope.launch {

                    addFavoriteToFirebase()

//                    if (!viewModel.isMealExists(it)) {
//                        viewModel.insertMeal(it)
//                            Toast.makeText(this@MealActivity, "Meal Saved", Toast.LENGTH_SHORT).show()
//
//                    } else{
//                        Toast.makeText(this@MealActivity, "Meal already saved", Toast.LENGTH_SHORT).show()
//                    }
                }

            }
        }
    }

    private fun onYoutubeImgClick() {
        binding.imgYoutube.setOnClickListener {
            val intent=Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private fun setInfoInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title=mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealInfoFromIntent() {
        val intent= intent
        mealId=intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName=intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb=intent.getStringExtra(HomeFragment.MEAL_THUMB)!!

    }

    private fun loadingCase(){

        binding.progressBar.visibility=View.VISIBLE
        binding.buttonFav.visibility=View.INVISIBLE
        binding.tvCategoryInfo.visibility=View.INVISIBLE
        binding.tvInstructions.visibility=View.INVISIBLE
        binding.tvAreaInfo.visibility=View.INVISIBLE
        binding.imgYoutube.visibility=View.INVISIBLE

    }

    private fun onResponseCase(){
        binding.progressBar.visibility=View.INVISIBLE
        binding.buttonFav.visibility=View.VISIBLE
        binding.tvCategoryInfo.visibility=View.VISIBLE
        binding.tvInstructions.visibility=View.VISIBLE
        binding.tvAreaInfo.visibility=View.VISIBLE
        binding.imgYoutube.visibility=View.VISIBLE

    }
    private fun addFavoriteToFirebase(){

        val hMap= hashMapOf<String,Any>()
        val hMapKey= hashMapOf<Any,Any>()
        val randomKey=UUID.randomUUID().toString() //meal key'e verilen eded ve reqemler

        hMap["mealName"]= mealToSave!!.strMeal
        hMap["mealImg"]=mealToSave!!.strMealThumb
        hMap["mealId"]=mealToSave!!.idMeal
        hMap["mealKey"]=randomKey

        hMapKey[randomKey]=hMap //key'in icindeki melumatlar

        Firebase.firestore.collection("Favorite Meals")
            .document(Firebase.auth.currentUser!!.uid).set(hMapKey, SetOptions.merge())

    }

    private fun checkFavoriteMeal(){


    }
}