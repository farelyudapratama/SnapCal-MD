package com.application.snapcal.data.response

data class ResetPassRequest (
    val password: String
)

data class ResetPassResponse (
    val status: String,
    val message: String,
)