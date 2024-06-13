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
import com.application.snapcal.data.response.DeleteAkun
import com.application.snapcal.data.response.ResponseProfile
import kotlinx.coroutines.launch

class ProfileViewModel (private val repository: Repository) : ViewModel() {
    private val _profileResult = MutableLiveData<ResultState<ResponseProfile>>()
    val profileItem: LiveData<ResultState<ResponseProfile>> = _profileResult

    private val _deleteAccountResult = MutableLiveData<ResultState<DeleteAkun>>()
    val deleteAccountResult: MutableLiveData<ResultState<DeleteAkun>> = _deleteAccountResult

    private val _logOutResult = MutableLiveData<ResultState<DeleteAkun>>()
    val logOutResult: MutableLiveData<ResultState<DeleteAkun>> = _logOutResult

    fun deleteAccount() {
        viewModelScope.launch {
            _deleteAccountResult.value = ResultState.Loading
            try {
                val response = repository.deleteAkun()
                _deleteAccountResult.value = response
            } catch (e: Exception) {
                _deleteAccountResult.value = ResultState.Error(e.message.toString())
            }
        }
    }
    init {
        getProfile()
    }

    private fun getProfile() {
        viewModelScope.launch {
            val profileResponse = repository.getProfileDetails()
            profileResponse.asFlow().collect { _profileResult.value = it }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            _logOutResult.value = ResultState.Loading
            try {
                val response = repository.logoutUser()
                _logOutResult.value = response
            } catch (e: Exception) {
                _logOutResult.value = ResultState.Error(e.message.toString())
            }
        }

//        viewModelScope.launch {
//            repository.logout()
//        }
    }
}