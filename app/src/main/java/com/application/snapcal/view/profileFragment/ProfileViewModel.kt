package com.application.snapcal.view.profileFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.application.snapcal.data.Repository
import com.application.snapcal.data.ResultState
import com.application.snapcal.data.pref.UserModel
import com.application.snapcal.data.response.ResponseProfile
import kotlinx.coroutines.launch

class ProfileViewModel (private val repository: Repository) : ViewModel() {
    private val _profileResult = MutableLiveData<ResultState<ResponseProfile>>()
    val profileItem: LiveData<ResultState<ResponseProfile>> = _profileResult

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

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}