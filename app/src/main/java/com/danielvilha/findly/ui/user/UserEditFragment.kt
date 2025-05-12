package com.danielvilha.findly.ui.user

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danielvilha.presentation.ui.user.UserEditScreen
import com.danielvilha.theme.FindlyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserEditFragment : Fragment() {

    private val viewModel: UserEditViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FindlyTheme {
                    CreateUserEditScreen()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setNavigationCallback { route ->
            when(route) {
                "onBackPressed" -> requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    @Composable
    private fun CreateUserEditScreen() {
        val state by viewModel.state.collectAsStateWithLifecycle()
        UserEditScreen(state = state)
    }
}