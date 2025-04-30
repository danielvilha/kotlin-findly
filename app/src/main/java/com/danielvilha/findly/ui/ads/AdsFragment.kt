package com.danielvilha.findly.ui.ads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danielvilha.presentation.ui.ads.AdsScreen
import com.danielvilha.theme.FindlyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdsFragment : Fragment() {

    private val viewModel: AdsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                FindlyTheme {
                    CreateAdsScreen()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setNavigationCallback { route ->
            when (route) {
                "onBackPressed" -> requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    @Composable
    private fun CreateAdsScreen() {
        val state by viewModel.state.collectAsStateWithLifecycle()
        AdsScreen(state = state)
    }
}