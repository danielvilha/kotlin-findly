package com.danielvilha.findly.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.danielvilha.findly.R
import com.danielvilha.presentation.ui.login.LoginScreen
import com.danielvilha.theme.FindlyTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val isUserLoggedIn = FirebaseAuth.getInstance().currentUser != null
        if (isUserLoggedIn) {
            findNavController().navigate(R.id.homeFragment)
        } else {
            viewModel.setNavigationCallback { route ->
                when (route) {
                    "homeFragment" -> findNavController().navigate(R.id.homeFragment)
                    "registerFragment" -> findNavController().navigate(R.id.registerFragment)
                    "signInWithGoogleInternal" -> viewModel.signInWithGoogleInternal(requireActivity())
                }
            }

            view.findViewById<ComposeView>(R.id.compose_view).setContent {
                FindlyTheme {
                    CreateLoginScreen()
                }
            }
        }
    }

    @Composable
    private fun CreateLoginScreen() {
        val state by viewModel.state.collectAsStateWithLifecycle()
        LoginScreen(state = state)
    }
}