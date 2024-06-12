package com.application.snapcal.data.api

import com.application.snapcal.data.response.ApiResponse
import com.application.snapcal.data.response.LoginRequest
import com.application.snapcal.data.response.LoginResponse
import com.application.snapcal.data.response.RegisterRequest
import com.application.snapcal.data.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST


interface ApiService {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
    @POST("logout")
    suspend fun logout(@Header("Authorization") token: String): ApiResponse
}