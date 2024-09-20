package com.example.waux.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.waux.R
import com.example.waux.ui.theme.WauxTheme

@Composable
fun ProfilePageView(
    navController: NavController,
    modifier: Modifier
) {
    var enableDeleteAccountDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("Guest User") }
    var email by remember { mutableStateOf("") }
    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileBackgroundImageView()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = name?: "",
                fontSize = 18.sp,
                color = Color.Black
            )

            Text(
                text = email?: "",
                fontSize = 20.sp,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {

                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(width = 104.dp, height = 48.dp)
                ) {
                    Text(
                        "Logout",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { enableDeleteAccountDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(width = 175.dp, height = 48.dp)
                ) {
                    Text(
                        "Delete Account",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (enableDeleteAccountDialog) {
            DeleteAccountDialogView(enableDeleteAccountDialog = enableDeleteAccountDialog)
        }
    }
}

@Composable
fun ProfileBackgroundImageView() {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color.LightGray)
            )
        }

        Box(
            modifier = Modifier
                .padding(top = 100.dp)
            ,
            contentAlignment = Alignment.TopCenter
        ) {
            Box (
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    modifier = Modifier.size(100.dp)
                ) {}
                Icon(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = "Person",
                    tint = Color.LightGray,
                    modifier = Modifier
                        .size(100.dp)
                )
            }
        }
    }
}

@Composable
fun DeleteAccountDialogView(enableDeleteAccountDialog: Boolean) {
    // Implement dialog logic here
    Text("hi")
    // You can use AlertDialog or a custom composable dialog as per your requirements
}

@Preview(showBackground = true)
@Composable
fun ProfilePagePreview() {
    WauxTheme {
        ProfilePageView(
            navController = rememberNavController(),
            modifier = Modifier
        )
    }
}
