package com.example.waux

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.waux.components.BottomNavigationBar
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

        var startPage = "home"
        val sessionId: StateFlow<String?> = repository.sessionId

        // Handle intent when the activity is first created
        if (intent?.action == Intent.ACTION_SEND) {
            if (sessionId.value != null) {
                startPage = "session"
                handleSendIntent(intent, repository, shareViewModel)
            }
        }

        setContent {
            WauxTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        startPage = startPage,
                        viewModels = viewModels,
                        repository = repository
                    )
                }
            }
        }
    }

    private fun handleSendIntent(intent: Intent, repository: UserRepository, viewModel: ShareViewModel) {
        when (intent.type) {
            "text/plain" -> {
                val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
                if (sharedText != null) {
                    viewModel.fetchTitleTask(url = sharedText, onResult = {})
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
                    // Handle the shared image URI
                }
            }
        }
    }
}


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    startPage: String = "home",
    viewModels:  Map<String, ViewModel>,
    repository: UserRepository
) {
    // Simulating login state
    // var isLoggedIn by remember { mutableStateOf(false) }
    // Observe the user state from the repository using collectAsState
    val user by repository.user.collectAsState(initial = null) // Provide an initial value (null)

    val navController = rememberNavController()

    if(user == null) {
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
            // Show main screen after login
            Scaffold(
                bottomBar = { BottomNavigationBar(navController) }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = startPage,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("home") { HomePageView(viewModel = viewModels["sessionViewModel"] as SessionViewModel, repository = repository) }
                    composable("session") { SessionPageView(userRepository = repository) }
                    composable("profile") { ProfilePageView(navController, modifier = Modifier) }
                }
            }
        }
    }

}
