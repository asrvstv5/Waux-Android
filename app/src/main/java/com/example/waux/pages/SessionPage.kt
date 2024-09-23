package com.example.waux.pages

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.runtime.*

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.waux.R
import com.example.waux.components.sessionPageComponents.dragContainer
import com.example.waux.components.sessionPageComponents.draggableItems
import com.example.waux.components.sessionPageComponents.rememberDragDropState
import com.example.waux.data.model.Playlist
import com.example.waux.data.model.SongEntry
import com.example.waux.domain.repository.UserRepository
import com.example.waux.network.SocketClient
import com.example.waux.viewModels.sessionViewModel.SessionViewModel

@Composable
fun SessionPageView(
    viewModel: SessionViewModel,
    sharedText: String?,
    userRepository: UserRepository,  // Injected userRepository
    modifier: Modifier = Modifier
) {
    // Collect the session and playlist data from the repository
    val session by userRepository.sessionData.collectAsState(initial = null)
    val playlist by userRepository.playlist.collectAsState(initial = Playlist())
    // val webSocketClient = WebSocketClient("ws://10.0.2.2:5000/socket.io/?EIO=4&transport=websocket")
    // val socketClient = SocketClient();
    LaunchedEffect(Unit) {
        session?.let {
            // Get the session
            viewModel.refreshSession()
            viewModel.socket_connect(sessionId = session!!.id, userId = userRepository.user.value!!.userId)
            //socketClient.connect(sessionId = session!!.id, userId = userRepository.user.value!!.userId)
            if (sharedText != null) {
                Log.d("ShareTest", "Session page View calling socket add song")
                viewModel.socket_addSong(sharedText)
            }
        }
    }

    // Handle cleanup (WebSocket closure) when the Composable is removed from the composition
    DisposableEffect(Unit) {
        onDispose {
            viewModel.socket_disconnect()
            // socketClient.disconnect()
        }
    }

    Box {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Display session name from repository
            Text(
                text = session?.name ?: "Session Name",
                fontSize = 20.sp
            )

            // Display session name from repository
            Text(
                text = session?.id ?: "Session Id",
                fontSize = 12.sp
            )

            // Display playlist image and song title (placeholder if no data yet)
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .width(50.dp)
                    .height(50.dp)
            )

            // If playlist has songs, show the current playing song, otherwise show a placeholder
            Text(
                text = playlist.songList.firstOrNull()?.song?.name ?: "No song playing",
                fontSize = 20.sp,
                modifier = Modifier.padding(16.dp)
                    .padding(top = 24.dp)
            )

            // This row component can be ignored for now
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                // Music service icon (e.g., Spotify)
                Image(
                    painter = painterResource(id = R.drawable.spotify_colored),
                    contentDescription = null,
                    modifier = Modifier.width(35.dp)
                )
                Spacer(Modifier.width(48.dp))

                // Music controls (skip previous, pause, skip next)
                Icon(
                    imageVector = Icons.Rounded.SkipPrevious,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 16.dp).size(35.dp)
                )
                Icon(
                    imageVector = Icons.Rounded.Pause,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 16.dp).size(35.dp)
                )
                Icon(
                    imageVector = Icons.Rounded.SkipNext,
                    contentDescription = null,
                    modifier = Modifier.size(35.dp)
                )
                Spacer(Modifier.width(48.dp))

                // Show the user initials (placeholder: GU)
                Text("GU", fontSize = 20.sp, modifier = Modifier.padding(8.dp))
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Draggable playlist items
            var list1  = playlist.songList
            val draggableItems by remember { derivedStateOf { list1.size } }
            val stateList = rememberLazyListState()

            val dragDropState = rememberDragDropState(
                lazyListState = stateList,
                draggableItemsNum = draggableItems,
                onMove = { fromIndex, toIndex ->
                    // Safeguard: Check if the indices are within the bounds of the list
                    if (fromIndex in list1.indices && toIndex in list1.indices) {
                        // Log for debugging
                        Log.d("DragDrop", "Moving item from $fromIndex to $toIndex")

                        // Perform the reordering
                        list1 = list1.toMutableList().apply {
                            add(toIndex, removeAt(fromIndex))
                        }

                        // Create a new playlist and update the repository
                        val newPlaylist = playlist.copy(songList = list1)
                        userRepository.savePlaylist(newPlaylist)
                    } else {
                        // Log if the indices are out of bounds
                        Log.e("DragDrop", "Index out of bounds: fromIndex=$fromIndex, toIndex=$toIndex, list size=${list1.size}")
                    }
                }
            )

            // List of song entries. This should update based on the message sent by websocket
            LazyColumn(
                modifier = Modifier.dragContainer(dragDropState),
                state = stateList,
            ) {
                draggableItems(items = list1, dragDropState = dragDropState) { modifier, item ->
                    item.id?.let {
                        Item(
                            modifier = modifier,
                            index = it,  // Use song id for uniqueness
                            songEntry = item
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Item(
    modifier: Modifier = Modifier,
    index: Int,
    source: String = "spotify",
    songEntry: SongEntry
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .drawBehind {
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            }
    ) {
        Row {
            // Icon of the music platform of song added.
            Image(
                painter = painterResource(id = R.drawable.spotify),
                contentDescription = "Spotify logo",
                modifier = Modifier.padding(top = 14.dp, start = 16.dp).width(25.dp)
            )
            // Name of the song added
            Text(
                text = songEntry.song.name,
                fontSize = 16.sp,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            // Name of the person who added the song
            Text("GU", modifier = Modifier.padding(16.dp))
            // Drag handle
            Icon(
                imageVector = Icons.Default.DragHandle,
                contentDescription = "Drag Handle",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
