package com.example.waux.components.loginpagecomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun LoginButton(modifier: Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .width(275.dp)
                .height(54.dp)
                .clip(shape = RoundedCornerShape(size = 8.dp))
                .background(Color(0xff777777))
        )
        Text(
            text = "Login",
            fontSize = 18.sp,
            color = Color.White
        )
    }
}

@Composable
fun GuestLoginButton(modifier: Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .width(275.dp)
                .height(54.dp)
                .clip(shape = RoundedCornerShape(size = 8.dp))
                .background(Color(0xff777777))
        )
        Text(
            text = "Guest Login",
            fontSize = 18.sp,
            color = Color.White
        )
    }
}
