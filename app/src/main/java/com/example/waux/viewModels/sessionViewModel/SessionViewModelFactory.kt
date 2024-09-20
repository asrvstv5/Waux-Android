package com.example.waux.viewModels.sessionViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.waux.domain.repository.UserRepository
import com.example.waux.viewModels.loginViewModel.LoginViewModel


class SessionViewModelFactory(
    private val userRepository: UserRepository,
    private val application: Application // Use Application context here
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SessionViewModel::class.java)) {
            return SessionViewModel(userRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}