package com.application.snapcal.data.api

import com.application.snapcal.data.response.DeleteAkun
import com.application.snapcal.data.response.LoginRequest
import com.application.snapcal.data.response.LoginResponse
import com.application.snapcal.data.response.RegisterRequest
import com.application.snapcal.data.response.RegisterResponse
import com.application.snapcal.data.response.ResetPassRequest
import com.application.snapcal.data.response.ResetPassResponse
import com.application.snapcal.data.response.ResponseProfile
import com.application.snapcal.data.response.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part


interface ApiService {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

//    @POST("logout")
//    suspend fun logout(@Header("Authorization") token: String): ApiResponse

    // Profile detail API
    @GET("user/profile-details")
    suspend fun getProfileDetails(): ResponseProfile

    @PUT( "user/profile-details")
    suspend fun updateProfileDetails(@Body request: ResponseProfile): ResponseProfile

    @Multipart
    @POST("user/profile-details/upload-photo")
    fun uploadProfilePhoto(
//        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part
    ): Call<UploadResponse>

    @PUT("auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPassRequest): ResetPassResponse

    @POST("auth/logout")
    suspend fun logout(): DeleteAkun

    @DELETE("auth/delete-account")
    suspend fun deleteAccount(): DeleteAkun

}