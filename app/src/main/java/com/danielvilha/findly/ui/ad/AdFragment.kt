package com.danielvilha.findly.ui.ad

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
import com.danielvilha.presentation.ui.ad.AdScreen
import com.danielvilha.model.AdMode
import com.danielvilha.theme.FindlyTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdFragment : Fragment() {

    private val viewModel: AdViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                FindlyTheme {
                    CreateAdScreen()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getString("id")
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        var mode = when {
            viewModel.state.value.ad.id.isNullOrBlank() -> AdMode.CREATE
            viewModel.state.value.ad.userId == userId -> AdMode.EDIT
            else -> AdMode.VIEW
        }
        viewModel.onIdChange(id)
        viewModel.onUserIdChange(userId)
        viewModel.state.value.mode = mode
        if ((mode == AdMode.EDIT || mode == AdMode.CREATE) && id?.isNotBlank() != null) {
            viewModel.loadAd(id.toString())
        }

        viewModel.setNavigationCallback { route ->
            when (route) {
                "onBackPressed" -> requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    @Composable
    private fun CreateAdScreen() {
        val state by viewModel.state.collectAsStateWithLifecycle()
        AdScreen(state = state)
    }
}