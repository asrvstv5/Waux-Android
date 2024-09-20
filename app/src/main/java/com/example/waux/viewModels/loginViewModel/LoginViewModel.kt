package com.example.waux.viewModels.loginViewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.waux.data.model.User
import com.example.waux.domain.repository.UserRepository
import com.example.waux.network.RetrofitBuilder
import com.example.waux.network.models.LoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class LoginViewModel(
    private val userRepository: UserRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _prefName = "user_prefs"

    // SharedPreferences instance to store refresh token
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences(_prefName, Context.MODE_PRIVATE)

    fun login(
        email: String? = null,
        password: String? = null,
        isGuestUser: Boolean,
        onResult: (String) -> Unit) {
        if(isGuestUser) {
            viewModelScope.launch {
                try {
                    // Make a call to login
                    val apiService = RetrofitBuilder.apiService
                    val loginResponse = apiService.login(guestUser = "true", request = LoginRequest())
                    // Store the jwt token, username, user_id in userRepository
                    val user: User = User(
                        userId = loginResponse.userId,
                        username = loginResponse.username
                    )
                    userRepository.saveJwtToken(token = loginResponse.token);
                    userRepository.saveUser(user = user);
                    // Pass the result back on the main thread
                    withContext(Dispatchers.Main) {
                        onResult(loginResponse.message)
                    }
                } catch (e: Exception) {
                    Log.e("loginViewModel", "login", e)
                    withContext(Dispatchers.Main) {
                        onResult("Error: ${e.message}")
                    }
                }
            }
        }
    }

}