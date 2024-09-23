package com.example.waux

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.waux.components.BottomNavigationBar
import com.example.waux.data.model.Session
import com.example.waux.data.model.User
import com.example.waux.domain.repository.UserRepository
import com.example.waux.pages.HomePageView
import com.example.waux.pages.LoginPageView
import com.example.waux.pages.ProfilePageView
import com.example.waux.pages.SessionPageView
import com.example.waux.ui.theme.WauxTheme
import com.example.waux.viewModels.loginViewModel.LoginViewModel
import com.example.waux.viewModels.loginViewModel.LoginViewModelFactory
import com.example.waux.viewModels.sessionViewModel.SessionViewModel
import com.example.waux.viewModels.sessionViewModel.SessionViewModelFactory
import com.example.waux.viewModels.shareViewModel.ShareViewModel
import com.example.waux.viewModels.shareViewModel.ShareViewModelFactory
import kotlinx.coroutines.flow.StateFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val repository = UserRepository(sharedPreferences)
        val shareViewModelFactory = ShareViewModelFactory(repository, application)
        val loginViewModelFactory = LoginViewModelFactory(repository, application)
        val sessionViewModelFactory = SessionViewModelFactory(repository, application)

        val shareViewModel = ViewModelProvider(this, shareViewModelFactory)[ShareViewModel::class.java]
        val loginViewModel = ViewModelProvider(this, loginViewModelFactory)[LoginViewModel::class.java]
        val sessionViewModel = ViewModelProvider(this, sessionViewModelFactory)[SessionViewModel::class.java]

        // Create a dictionary (Map) of ViewModels
        val viewModels: Map<String, ViewModel> = mapOf(
            "shareViewModel" to shareViewModel,
            "loginViewModel" to loginViewModel,
            "sessionViewModel" to sessionViewModel
        )

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            WauxTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModels = viewModels,
                        repository = repository,
                        navController = navController,
                        intent = intent,
                    )
                }
            }
        }
    }
}


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModels:  Map<String, ViewModel>,
    repository: UserRepository,
    navController: NavHostController,
    intent: Intent?,
) {

    Log.d("ShareSong", "mainscreen started")
    if (intent != null) {
        Log.d("ShareSong", "intent is ${intent.type}")
    } else {
        Log.d("ShareSong", "intent is null on mainscreen")
    }
    // Simulating login state
    // var isLoggedIn by remember { mutableStateOf(false) }
    // Observe the user state from the repository using collectAsState
    val user by repository.user.collectAsState(initial = null) // Provide an initial value (null)

    if(user == null) {
        // Check if refresh Token exists.
        Log.d("ShareSong", "user null")
        LoginPageView(
            viewModel = viewModels["loginViewModel"] as LoginViewModel
        )
    }
    if (user!=null) {
        if(user?.userId == "") {
            // Show login page if not logged in
            LoginPageView(
                viewModel = viewModels["loginViewModel"] as LoginViewModel
            )
        } else {
            Log.d("ShareSong", "login done")
            var startPage = "home"
            // Handle intent when the activity is first created
            if (intent?.action == Intent.ACTION_SEND) {
                Log.d("ShareSong", "got intent")
                // Pass the navController here to handle navigation inside composables
                HandleSendIntent(intent, repository, viewModels["shareViewModel"] as ShareViewModel, navController)
            } else {
                Log.d("ShareSong", "no intent")
            }
            // Show main screen after login
            Scaffold(
                bottomBar = { BottomNavigationBar(navController) }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = startPage,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(
                        route = "home"
                    ) { HomePageView(viewModel = viewModels["sessionViewModel"] as SessionViewModel, repository = repository) }

                    composable(
                        route = "session?sharedText={sharedText}",
                        arguments = listOf(navArgument("sharedText") { nullable = true; type = NavType.StringType })
                    ) { backStackEntry ->
                        val sharedText = backStackEntry.arguments?.getString("sharedText")
                        SessionPageView(
                            viewModel = viewModels["sessionViewModel"] as SessionViewModel,
                            userRepository = repository,
                            sharedText = sharedText
                        )
                    }

                    composable("profile") { ProfilePageView(navController, modifier = Modifier) }
                }
            }
        }
    }

}

@Composable
fun HandleSendIntent(
    intent: Intent,
    repository: UserRepository,
    shareViewModel: ShareViewModel,
    navController: NavController
) {
    Log.d("ShareSong", "handling send intent")
    // Check for sessionId.
    val session by shareViewModel.session.collectAsState();
    when (intent.type) {
        "text/plain" -> {
            val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
            if (sharedText != null && session?.id != null && session?.id != "") {
                // Navigate to the session page with the shared text
                shareViewModel.saveSharedText(sharedText);
                navController.navigate("session?sharedText=${Uri.encode(sharedText)}")
            }
        }
        "image/*" -> {
            val imageUri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(Intent.EXTRA_STREAM)
            }

            if (imageUri != null) {
                // Handle image URI, navigate if necessary
            }
        }
    }
}
