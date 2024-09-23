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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.waux.R
import com.example.waux.components.loginpagecomponents.GuestLoginButton
import com.example.waux.components.loginpagecomponents.LoginButton
import com.example.waux.ui.theme.WauxTheme
import com.example.waux.viewModels.loginViewModel.LoginViewModel

@Composable
fun LoginPageView(
    viewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {

    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(Unit) {
        viewModel.autoLogin()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
        )
        Text(
            "WAUX",
            fontSize = 28.sp,
            modifier = Modifier.padding(24.dp)
        )
        Spacer(modifier = Modifier.height(28.dp))
        TextField(
            value = email,
            textStyle = TextStyle(fontSize = 16.sp),
            singleLine = true,
            onValueChange = { email = it },
            modifier = Modifier
                .width(275.dp)
                .padding(bottom = 16.dp)
                .clip(shape = RoundedCornerShape(size = 8.dp)),
            placeholder = {
                Text(
                    "Email",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            },
            colors = TextFieldDefaults.colors(
                // Make the indicator transparent to remove the bottom border
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        var showPassword by remember { mutableStateOf(false) }
        TextField(
            value = password,
            textStyle = TextStyle(fontSize = 16.sp),
            singleLine = true,
            onValueChange = { password = it },
            colors = TextFieldDefaults.colors(
                // Make the indicator transparent to remove the bottom border
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .width(275.dp)
                .padding(bottom = 16.dp)
                .clip(shape = RoundedCornerShape(size = 8.dp)),
            visualTransformation = if (showPassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            }, // Masks the input
            placeholder = {
                Text(
                    "Password",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            },
            trailingIcon = {
                Icon(
                    if (showPassword) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    },
                    contentDescription = "Toggle password visibility",
                    modifier = Modifier
                        .clickable { showPassword = !showPassword }
                        .padding(end = 8.dp)
                )
            }
        )
        LoginButton(
            modifier = Modifier
                .clickable {

                }
                .padding(bottom = 12.dp)
        )

        GuestLoginButton(
            modifier = Modifier
                .clickable {
                    viewModel.login(isGuestUser = true, onResult = {})
                }
                .padding(bottom = 24.dp)
        )

        Row {
            Text("Don't have an account?  ", color = Color.White)
            Text(
                "Sign Up",
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun LoginPageViewPreview() {
    WauxTheme {
        LoginPageView(, {})
    }
}*/