package com.application.snapcal.view.editProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.application.snapcal.data.Repository
import com.application.snapcal.data.ResultState
import com.application.snapcal.data.response.Data
import com.application.snapcal.data.response.ResponseProfile
import com.application.snapcal.data.response.UploadResponse
import kotlinx.coroutines.launch
import java.io.File

class EditProfileViewModel(private val repository: Repository) : ViewModel() {
    private val _profileResult = MutableLiveData<ResultState<ResponseProfile>>()
    val profileResult: MutableLiveData<ResultState<ResponseProfile>> = _profileResult
    private var userId: String? = null
    fun uploadPhoto(filePath: File): LiveData<UploadResponse> {
        return repository.uploadPhoto(filePath)
    }
    init {
        getProfile()
    }

    private fun getProfile() {
        viewModelScope.launch {
            val profileResponse = repository.getProfileDetails()
            profileResponse.asFlow().collect {
                _profileResult.value = it
            }
        }
    }
//    fun uploadProfilePhoto(file: File) = repository.uploadImage(file)
    fun saveProfileChanges(name: String, email: String, gender: String?, weight: Int?, height: Int?, age: Int?) {
        val userProfile = ResponseProfile(
            userId = userId ?: "",
            data = Data(
                name = name,
                email = email,
                gender = gender,
                beratBadan = weight,
                tinggiBadan = height,
                usiaUser = age
            )
        )
        updateProfile(userProfile)
    }

    private fun updateProfile(userProfile: ResponseProfile) {
        viewModelScope.launch {
            _profileResult.value = ResultState.Loading
            try {
                val profileResponse = repository.updateProfile(userProfile)
                _profileResult.value = profileResponse
            } catch (e: Exception) {
                _profileResult.value = ResultState.Error(e.toString())
            }
        }
    }
}