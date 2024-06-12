package com.application.snapcal.data.response

data class LoginRequest(
	val email: String,
	val password: String
)

data class LoginResponse(
	val status: String,
	val message: String,
	val token: String
)
