package com.example.waux.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.waux.R
import com.example.waux.components.homepagecomponents.JoinSessionButton
import com.example.waux.components.loginpagecomponents.GuestLoginButton
import com.example.waux.components.loginpagecomponents.LoginButton
import com.example.waux.domain.repository.UserRepository
import com.example.waux.ui.theme.WauxTheme
import com.example.waux.viewModels.sessionViewModel.SessionViewModel

@Composable
fun HomePageView(
    repository: UserRepository,
    viewModel: SessionViewModel,
    modifier: Modifier = Modifier
) {

    var sessionId by remember { mutableStateOf(TextFieldValue("")) }
    val user by repository.user.collectAsState()

    Box {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
            )
            Text("WAUX", fontSize = 28.sp, modifier = Modifier.padding(24.dp).padding(bottom = 48.dp))
            user?.username?.let { Text(it) }
            TextField(
                value = sessionId,
                onValueChange = { sessionId = it },
                modifier = Modifier.width(350.dp).padding(bottom = 16.dp),
                placeholder = {
                    Text("Session name", color = Color.Gray, fontSize = 16.sp)
                },
                colors = TextFieldDefaults.colors(
                    // Make the indicator transparent to remove the bottom border
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            JoinSessionButton(
                modifier = Modifier.clickable {
                    repository.joinSession(sessionId.text)
                    viewModel.joinSession(sessionId = sessionId.text, onResult = {})
                    // once I join the session first move to session page
                    // Then on the session page, populate the lazy column from the data in
                    // the playlist.
                    // Then, start a websocket to get real time updates from the db.
                    // In share controller, when a song is added, instead of populating
                    // the local variables (as is now), make an api call to add song to the
                    // playlist. This should update the playlist on its on due to the web-socket
                }.padding(bottom = 12.dp)
            )
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun HomePageViewPreview() {
    WauxTheme {
        HomePageView()
    }
}
 */