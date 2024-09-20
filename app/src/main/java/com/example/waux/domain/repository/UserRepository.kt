package com.example.waux.domain.repository

import android.content.SharedPreferences
import com.example.waux.data.model.Playlist
import com.example.waux.data.model.Session
import com.example.waux.data.model.Song
import com.example.waux.data.model.SongEntry
import com.example.waux.data.model.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// domain/repository/UserRepository.kt
open class UserRepository(private val sharedPreferences: SharedPreferences) {
    private var _user = MutableStateFlow<User?>(null)
    var user: StateFlow<User?> = _user

    private var _sessionId = MutableStateFlow<String?>(null)
    var sessionId: StateFlow<String?> = _sessionId

    private var _session = MutableStateFlow<Session?>(null)
    var sessionData: StateFlow<Session?> = _session

    private var _playlist = MutableStateFlow<Playlist>(Playlist())
    var playlist: StateFlow<Playlist> = _playlist

    private var _refreshToken = MutableStateFlow<String?>(null)
    var refreshToken: StateFlow<String?> = _refreshToken

    private val _keyJwtToken = "waux_jwt_token"
    private val _keyRefreshToken = "waux_refresh_token"

    // Helper functions for tokens and user info (as in your current code)
    fun getJwtToken(): String? = sharedPreferences.getString(_keyJwtToken, null)

    fun saveJwtToken(token: String?) {
        sharedPreferences.edit().putString(_keyJwtToken, token).apply()
    }

    fun saveRefreshToken(refreshToken: String) {
        _refreshToken.value = refreshToken
        sharedPreferences.edit().putString(_keyRefreshToken, refreshToken).apply()
    }

    fun saveUser(user: User) {
        _user.value = user
    }

    // Session-related functions
    fun joinSession(sessionName: String) {
        // API call to join or create session
        // Populate sessionId and playlist
        // Example pseudo-code for API response handling:
        _sessionId.value = sessionName // or response.sessionId
        _playlist.value = Playlist() // or response.playlist
    }

    fun savePlaylist(playlist: Playlist) {
        _playlist.value = playlist
    }

    fun saveSession(session: Session) {
        _session.value = session
    }

    fun leaveSession() {
        // Clear session details
        _sessionId.value = null
        _playlist.value = Playlist()
        // Close WebSocket or API connection if applicable
    }

    // Function to handle adding a song when content is shared from YouTube/Spotify
    fun addSongToPlaylist(songUri: String, songName: String, author: String) {
        val newSong = Song(
            uri = songUri,
            name = songName
        )
        val newEntry = SongEntry(
            song = newSong,
            author = author,
            id = _playlist.value.songList.size
        )
        _playlist.value = _playlist.value.copy(
            songList = _playlist.value.songList + newEntry
        )
        // Notify the backend about the new song (e.g., via WebSocket or API call)
    }

    fun logout() {
        clearRefreshToken();
        _refreshToken.value = null;
        clearJwtToken();
        clearRefreshToken();
    }

    fun deleteAccount() {
        clearJwtToken();
        _refreshToken.value = null;
        clearRefreshToken();
    }

    fun setRefreshToken() {
        _refreshToken.value = sharedPreferences.getString(_keyRefreshToken, null)
    }

    // Function to clear the JWT token (e.g., on logout)
    private fun clearJwtToken() {
        sharedPreferences.edit().remove(_keyJwtToken).apply()
    }

    private fun clearRefreshToken() {
        sharedPreferences.edit().remove(_keyRefreshToken).apply()
    }

    fun addSong(songEntry: SongEntry) {
        // Make call to api to get session details again
    }

    fun deleteSong(songId: Int) {
        // Make call to api to get session details again
    }

    fun refreshPlaylist() {
        // Make call to api to get session details again
    }

    fun updateCurrentSong(songId: Int) {
        _playlist.value.currentSongId = songId
    }
}
