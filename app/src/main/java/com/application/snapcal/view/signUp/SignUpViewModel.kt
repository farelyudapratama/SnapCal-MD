package com.application.snapcal.view.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.snapcal.data.Repository
import com.application.snapcal.data.ResultState
import com.application.snapcal.data.response.RegisterResponse
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: Repository) : ViewModel() {

    private val _registerResult = MutableLiveData<ResultState<RegisterResponse>>()
    val signUpResult: LiveData<ResultState<RegisterResponse>> get() = _registerResult

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            _registerResult.value = ResultState.Loading
            try {
                val result = repository.register(name, email, password)
                _registerResult.value = result
            } catch (e: Exception) {
                _registerResult.value = ResultState.Error(e.toString())
            }
        }
    }
}