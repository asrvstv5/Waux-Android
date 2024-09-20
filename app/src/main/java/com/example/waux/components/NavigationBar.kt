package com.example.waux.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.waux.R

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Session,
        BottomNavItem.Profile
    )
    BottomNavigation (
        backgroundColor = Color(0xffd9d9d9)
    ) {
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    var alpha = 1.0F;
                    if(currentRoute != item.route) {
                        alpha = 0.4F
                    }
                    if (item.icon != null) {
                        Icon(imageVector = item.icon!!, contentDescription = item.title
                        , modifier = Modifier.alpha(alpha))
                    } else {
                        Image(
                            painter = painterResource(id = item.drawableRes!!),
                            contentDescription = item.title,
                            modifier = Modifier
                                .height(35.dp)
                                .width(35.dp)
                                .alpha(alpha)
                        )
                    }
                },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

enum class BottomNavItem(val route: String, val title: String, val icon: ImageVector? = null, val drawableRes: Int? = null) {
    Home("home", "Home", Icons.Default.Home),
    Session("session", "Session", drawableRes = R.drawable.headphones),  // Use drawableRes for PNG
    Profile("profile", "Profile", Icons.Default.Person)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    val navController = rememberNavController() // Create a navController for the preview
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        // You can display any content here to simulate the main screen
        Text("Main Screen")
    }
}