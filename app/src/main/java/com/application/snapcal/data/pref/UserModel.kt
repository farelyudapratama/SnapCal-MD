package com.application.snapcal.data.pref

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)