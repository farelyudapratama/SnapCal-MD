package com.application.snapcal.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.application.snapcal.data.api.ApiConfig
import com.application.snapcal.data.api.ApiConfig.Companion.token
import com.application.snapcal.data.api.ApiService
import com.application.snapcal.data.pref.UserModel
import com.application.snapcal.data.pref.UserPreference
import com.application.snapcal.data.response.DeleteAkun
import com.application.snapcal.data.response.LoginRequest
import com.application.snapcal.data.response.LoginResponse
import com.application.snapcal.data.response.RegisterRequest
import com.application.snapcal.data.response.RegisterResponse
import com.application.snapcal.data.response.ResetPassRequest
import com.application.snapcal.data.response.ResetPassResponse
import com.application.snapcal.data.response.ResponseProfile
import com.application.snapcal.data.response.UploadResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.File

class Repository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
){
    suspend fun saveSession(user: UserModel) = userPreference.saveSession(user)
    fun getSession() = userPreference.getSession()

    suspend fun logout() = userPreference.logout()

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

    suspend fun getProfileDetails()= liveData{
        emit(ResultState.Loading)
        try {
            val response = apiService.getProfileDetails()
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e.toString()))
        }
    }

    suspend fun updateProfile(userProfile: ResponseProfile): ResultState<ResponseProfile>{
        ResultState.Loading
        return try {
            val response = apiService.updateProfileDetails(userProfile)
            ResultState.Success(response)
        } catch (e: HttpException){
            ResultState.Error(e.toString())
        }
    }

//    fun uploadImage(imageFile: File) = liveData {
//        emit(ResultState.Loading)
//        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
//        val multipartBody = MultipartBody.Part.createFormData(
//            "photo",
//            imageFile.name,
//            requestImageFile
//        )
//        try {
//            val response = apiService.uploadProfilePhoto(multipartBody)
//            emit(ResultState.Success(response))
//        } catch (e: Exception) {
//            emit(ResultState.Error(e.toString()))
//        }
//    }
    fun uploadPhoto(imageFile: File): LiveData<UploadResponse> {
        val uploadPhotoResponse = MutableLiveData<UploadResponse>()

        val requestImageFile = imageFile.asRequestBody("image/*".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        ApiConfig.getApiService(token)

        val call: Call<UploadResponse> = apiService.uploadProfilePhoto( multipartBody)
        call.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                if (response.isSuccessful) {
                    uploadPhotoResponse.value = response.body()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("PhotoRepository", "Upload failed with status code ${response.code()}: $errorBody")
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                Log.e("PhotoRepository", "Upload error: ${t.message}")
            }
        })

        return uploadPhotoResponse
    }
    suspend fun resetPassword(newPassword: String): ResultState<ResetPassResponse> {
        return try {
            val request = ResetPassRequest(newPassword)
            val response = apiService.resetPassword(request)
            ResultState.Success(response)
        } catch (e: Exception) {
            ResultState.Error(e.toString())
        }
    }

    suspend fun logoutUser(): ResultState<DeleteAkun>{
        ResultState.Loading
        return try {
            val response = apiService.logout()
            userPreference.logout()
            ResultState.Success(response)
        } catch (e: Exception) {
            ResultState.Error(e.toString())
        }
    }


    suspend fun deleteAkun(): ResultState<DeleteAkun>{
        ResultState.Loading
        return try {
            val response = apiService.deleteAccount()
            userPreference.logout()
            ResultState.Success(response)
        } catch (e: Exception) {
            ResultState.Error(e.toString())
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