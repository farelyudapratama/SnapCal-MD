package com.application.snapcal.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.snapcal.data.Repository
import com.application.snapcal.data.ResultState
import com.application.snapcal.data.pref.UserModel
import com.application.snapcal.data.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {
    private val _loginResult = MutableLiveData<ResultState<LoginResponse>>()
    val loginResult: LiveData<ResultState<LoginResponse>> get() = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = ResultState.Loading
            try {
                val result = repository.login(email, password)
                _loginResult.value = result
            } catch (e: Exception) {
                _loginResult.value = ResultState.Error(e.toString())
            }
        }
    }

    fun saveSession(user: UserModel){
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}