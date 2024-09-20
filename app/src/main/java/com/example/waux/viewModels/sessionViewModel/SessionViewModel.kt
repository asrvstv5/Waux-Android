package com.example.waux.viewModels.sessionViewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.waux.data.model.Playlist
import com.example.waux.data.model.Session
import com.example.waux.data.model.User
import com.example.waux.domain.repository.UserRepository
import com.example.waux.network.RetrofitBuilder
import com.example.waux.network.models.JoinSessionRequest
import com.example.waux.network.models.LoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SessionViewModel(
    private val userRepository: UserRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _prefName = "user_prefs"

    // SharedPreferences instance to store refresh token
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences(_prefName, Context.MODE_PRIVATE)

    fun joinSession(
        sessionId: String,
        onResult: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Make a call to login
                val apiService = RetrofitBuilder.apiService
                val token = userRepository.getJwtToken()
                Log.w("SessionViewModel", "joinSession - got token")

                val joinSessionResponse = apiService.joinSession(
                    token = token!!,
                    request = JoinSessionRequest(sessionId = sessionId)
                )
                Log.w("SessionViewModel", "joinSession - got joinSessionResponse")
                // Store the jwt token, username, user_id in userRepository
                val session = Session(
                    id = joinSessionResponse.sessionId,
                    name = joinSessionResponse.name,
                    host = joinSessionResponse.host.userId
                )
                userRepository.saveSession(session)
                Log.w("SessionViewModel", "joinSession - saved session")

                userRepository.savePlaylist(playlist = joinSessionResponse.playlist)
                Log.w("SessionViewModel", "joinSession - saved playlist")

                // Pass the result back on the main thread
                withContext(Dispatchers.Main) {
                    onResult()
                }
            } catch (e: Exception) {
                Log.e("SessionViewModel", "joinSession", e)
                withContext(Dispatchers.Main) {
                    onResult()
                }
            }
        }
    }
}