package com.nezrin.tastyeats.di

import android.content.Context
import androidx.room.Room
import com.nezrin.tastyeats.common.Constant
import com.nezrin.tastyeats.data.local.MealDao
import com.nezrin.tastyeats.data.local.MealDatabase
import com.nezrin.tastyeats.data.network.MealApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ModuleApp {


    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit):MealApi{
        return retrofit.create(MealApi::class.java)
    }
    @Provides
    @Singleton
    fun provideDao(mealDB:MealDatabase):MealDao{
        return mealDB.mealDao()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor


    }

    @Provides
    @Singleton
    fun provideRetrofit(loggingInterceptor: HttpLoggingInterceptor): Retrofit {
        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor)
            .build()
        return Retrofit.Builder()
            .client(client)
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMealDatabase(@ApplicationContext context: Context):MealDatabase{
        val db = Room.databaseBuilder(context, MealDatabase::class.java, "my_database").build()
        return db
    }
}