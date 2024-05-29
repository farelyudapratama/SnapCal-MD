package com.application.snapcal.view.profileFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.snapcal.data.Repository
import kotlinx.coroutines.launch

class ProfileViewModel (private val repository: Repository) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}