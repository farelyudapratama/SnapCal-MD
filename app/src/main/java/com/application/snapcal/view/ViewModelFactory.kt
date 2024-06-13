package com.application.snapcal.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.snapcal.data.Repository
import com.application.snapcal.di.Injection
import com.application.snapcal.view.editProfile.EditProfileViewModel
import com.application.snapcal.view.login.LoginViewModel
import com.application.snapcal.view.main.MainViewModel
import com.application.snapcal.view.profileFragment.ProfileViewModel
import com.application.snapcal.view.signUp.SignUpViewModel
import com.application.snapcal.view.splash.SplashViewModel

class ViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                    MainViewModel(repository) as T
                }
                modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                    LoginViewModel(repository) as T
                }
                modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                    SignUpViewModel(repository) as T
                }
                modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                    SplashViewModel(repository) as T
                }
                modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                    ProfileViewModel(repository) as T
                }
                modelClass.isAssignableFrom(EditProfileViewModel::class.java) -> {
                    EditProfileViewModel(repository) as T
                }
                else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
            }
        }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}