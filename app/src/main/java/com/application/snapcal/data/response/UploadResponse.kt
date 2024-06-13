package com.application.snapcal.data.response

import com.google.gson.annotations.SerializedName

data class UploadResponse(

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
