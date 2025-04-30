package com.danielvilha.findly.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.danielvilha.findly.R
import com.danielvilha.presentation.ui.home.HomeScreen
import com.danielvilha.theme.FindlyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FindlyTheme {
                    CreateHomeScreen()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setNavigationCallback { route ->
            when(route) {
                "onAdClicked" -> {
                    val id = route.removePrefix("onAdClicked/")
                    val bundle = Bundle().apply {
                        putString("id", id)
                    }
                    findNavController().navigate(R.id.adFragment, bundle)
                }
                "onCreateAdClicked" -> findNavController().navigate(R.id.adFragment)
                "onNavigateToMyAdsClicked" -> findNavController().navigate(R.id.adsFragment)
                "onLogoutConfirmed" -> findNavController().navigate(R.id.loginFragment)
            }
        }
    }

    @Composable
    private fun CreateHomeScreen() {
        val state by viewModel.state.collectAsStateWithLifecycle()
        HomeScreen(state = state)
    }
}