package com.application.snapcal.data.response

import com.google.gson.annotations.SerializedName

data class ResponseProfile(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class UpdatedAt(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int? = null,

	@field:SerializedName("_seconds")
	val seconds: Int? = null
)

data class Data(

	@field:SerializedName("createdAt")
	val createdAt: CreatedAt? = null,

	@field:SerializedName("beratBadan")
	val beratBadan: Int? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("usiaUser")
	val usiaUser: Int? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("gambar_profil")
	val gambarProfil: String? = null,

	@field:SerializedName("tinggiBadan")
	val tinggiBadan: Int? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("token")
	val token: Token? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: UpdatedAt? = null
)

data class CreatedAt(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int? = null,

	@field:SerializedName("_seconds")
	val seconds: Int? = null
)

data class Token(

	@field:SerializedName("auth")
	val auth: String? = null,

	@field:SerializedName("forgetPass")
	val forgetPass: Any? = null
)
