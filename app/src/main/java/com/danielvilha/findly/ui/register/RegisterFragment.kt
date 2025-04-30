package com.danielvilha.findly.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.danielvilha.findly.R
import com.danielvilha.presentation.ui.register.RegisterScreen
import com.danielvilha.theme.FindlyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FindlyTheme {
                    CreateRegisterScreen()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.setNavigationCallback { route ->
            when (route) {
                "homeFragment" -> {
                    findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                }
                "loginFragment",
                "onBackPressed" -> requireActivity().onBackPressedDispatcher.onBackPressed()
                "onRegisterGoogleSignIn" -> Toast.makeText(context, "In develop", Toast.LENGTH_LONG).show()
            }
        }
    }

    @Composable
    private fun CreateRegisterScreen() {
        val state by viewModel.state.collectAsStateWithLifecycle()
        RegisterScreen(state = state)
    }
}