package com.application.snapcal.data

import com.application.snapcal.data.api.ApiConfig
import com.application.snapcal.data.api.ApiService
import com.application.snapcal.data.pref.UserModel
import com.application.snapcal.data.pref.UserPreference
import com.application.snapcal.data.response.ErrorResponse
import com.application.snapcal.data.response.LoginResponse
import com.application.snapcal.data.response.SignUpResponse
import com.google.gson.Gson
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
        try {
            val response = apiService.login(email, password)
            if (response.error == true) {
                return ResultState.Error(response.message ?: "Unknown Error")
            } else {
                val loginResult = response.loginResult
                if (loginResult != null) {
                    val sesi = UserModel(
                        email = email,
                        token = loginResult.token ?: "",
                        isLogin = true
                    )
                    saveSession(sesi)
                    ApiConfig.token = loginResult.token ?: ""
                    return ResultState.Success(response)
                } else {
                    return ResultState.Error("Login is nulll")
                }
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            return ResultState.Error(errorMessage ?: "Unknown Error")
        }
    }

    suspend fun register(name: String, email: String, password: String): ResultState<SignUpResponse>{
        ResultState.Loading
        return try{
            val response = apiService.register(name, email, password)
            if (response.error == true) {
                ResultState.Error(response.message ?: "Unknown Error")
            } else {
                ResultState.Success(response)
            }
        }catch (e: HttpException){
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            ResultState.Error(errorMessage.toString())
        }
    }

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