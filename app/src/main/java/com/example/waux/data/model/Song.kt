package com.example.waux.data.model

import java.util.UUID

data class Song (
    val uri: String = "",
    val name: String = ""
)

data class SongEntry (
    val song: Song = Song(),
    val author: String = "",
    val id: Int? = null
)

data class Playlist (
    var songList: List<SongEntry> = emptyList<SongEntry>(),
    val authorName: String = "",
    val sessionId: String = "",
    var currentSongId: Int = -1
)