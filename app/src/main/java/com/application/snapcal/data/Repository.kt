package com.application.snapcal.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.application.snapcal.data.api.ApiConfig
import com.application.snapcal.data.api.ApiService
import com.application.snapcal.data.pref.UserModel
import com.application.snapcal.data.pref.UserPreference
import com.application.snapcal.data.response.ApiResponse
import com.application.snapcal.data.response.LoginRequest
import com.application.snapcal.data.response.LoginResponse
import com.application.snapcal.data.response.RegisterRequest
import com.application.snapcal.data.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException

class Repository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
){
    suspend fun saveSession(user: UserModel) = userPreference.saveSession(user)
    fun getSession() = userPreference.getSession()

    suspend fun logout() = userPreference.logout()

    // Belum bener ke api
    suspend fun login(email: String, password: String): ResultState<LoginResponse>{
        ResultState.Loading
        return try {
            val request = LoginRequest(email, password)
            val response = apiService.login(request)
            val sesi = UserModel(
                email = email,
                token = response.token,
                isLogin = true
            )
            saveSession(sesi)
            ApiConfig.getApiService(sesi.token)
            ResultState.Success(response)
        } catch (e: HttpException) {
            ResultState.Error(e.toString())
        }
    }

    suspend fun register(name: String, email: String, password: String): ResultState<RegisterResponse>{
        ResultState.Loading
        return try{
            val request = RegisterRequest(name, email, password)
            val response = apiService.register(request)
            ResultState.Success(response)
        }catch (e: HttpException){
            ResultState.Error(e.toString())
        }
    }
//    suspend fun logoutUser(token: String): LiveData<ResultState<ApiResponse>> = liveData(Dispatchers.IO) {
//        emit(ResultState.Loading)
//        try {
//            val response = apiService.logout(token)
//            userPreference.logout()
//            emit(ResultState.Success(response))
//        } catch (e: Exception) {
//            emit(ResultState.Error(e.toString()))
//        }
//    }


    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, userPreference)
            }
    }
}