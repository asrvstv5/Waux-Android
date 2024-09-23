package com.example.waux.viewModels.shareViewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.waux.data.model.Session
import com.example.waux.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class ShareViewModel(
    private val userRepository: UserRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _prefName = "user_prefs"
    val session: StateFlow<Session?> = userRepository.sessionData;

    // SharedPreferences instance to store refresh token
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences(_prefName, Context.MODE_PRIVATE)

    fun saveSharedText(text: String?) {
        userRepository.saveSharedText(text = text)
    }

    fun fetchTitleTask(url: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // Switch to IO dispatcher for the network call
                val title = withContext(Dispatchers.IO) {
                    val document = Jsoup.connect(url).get()
                    document.title()
                }

                userRepository.addSongToPlaylist(
                    songUri = url,
                    songName = title,
                    author = "Unknown" // Customize as needed
                )
                // Pass the result back on the main thread
                withContext(Dispatchers.Main) {
                    onResult(title)
                }
            } catch (e: Exception) {
                Log.e("shareMusic", "Error during fetching title", e)
                withContext(Dispatchers.Main) {
                    onResult("Error: ${e.message}")
                }
            }
        }
    }
}