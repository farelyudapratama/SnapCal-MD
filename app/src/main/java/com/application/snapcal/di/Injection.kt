package com.application.snapcal.di

import android.content.Context
import com.application.snapcal.data.Repository
import com.application.snapcal.data.api.ApiConfig
import com.application.snapcal.data.pref.UserPreference
import com.application.snapcal.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return Repository.getInstance(apiService, pref)
    }
}