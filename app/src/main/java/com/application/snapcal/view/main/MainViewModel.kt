package com.application.snapcal.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.application.snapcal.data.Repository
import com.application.snapcal.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {


    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}