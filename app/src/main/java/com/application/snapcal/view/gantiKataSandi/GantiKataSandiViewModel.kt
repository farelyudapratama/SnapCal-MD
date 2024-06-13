package com.application.snapcal.view.gantiKataSandi

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.snapcal.data.Repository
import com.application.snapcal.data.ResultState
import com.application.snapcal.data.response.ResetPassResponse
import kotlinx.coroutines.launch

class GantiKataSandiViewModel(private val repository: Repository) : ViewModel() {
    private val _resetPasswordResult = MutableLiveData<ResultState<ResetPassResponse>>()
    val resetPasswordResult: MutableLiveData<ResultState<ResetPassResponse>> = _resetPasswordResult

    fun resetPassword(newPassword: String) {
        viewModelScope.launch {
            _resetPasswordResult.value = ResultState.Loading
            val result = repository.resetPassword(newPassword)
            _resetPasswordResult.value = result
        }
    }
}