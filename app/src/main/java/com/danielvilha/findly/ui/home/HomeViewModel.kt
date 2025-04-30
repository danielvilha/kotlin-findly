package com.danielvilha.findly.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielvilha.model.repository.AdRepositoryImpl
import com.danielvilha.model.repository.AdResult
import com.danielvilha.presentation.ui.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AdRepositoryImpl
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    private var onNavigate: ((String) -> Unit)? = null

    init {
        _state.value = _state.value.copy(
            onAdClick = ::onAdClicked,
            onCreateAd = ::onCreateAdClicked,
            onNavigateToMyAds = ::onNavigateToMyAdsClicked,
            onLogoutConfirmed = ::onLogoutConfirmed
        )
        loadAds()
    }

    fun setNavigationCallback(callback: (String) -> Unit) {
        onNavigate = callback
    }

    private fun onAdClicked(id: String) {
        onNavigate?.invoke("onAdClicked/$id")
    }

    private fun onCreateAdClicked() {
        onNavigate?.invoke("onCreateAdClicked")
    }

    private fun onNavigateToMyAdsClicked() {
        onNavigate?.invoke("onNavigateToMyAdsClicked")
    }

    private fun onLogoutConfirmed() {
        onNavigate?.invoke("onLogoutConfirmed")
    }

    private fun loadAds() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when(val result = repository.loadAds()) {
                is AdResult.SuccessList -> {
                    _state.value = _state.value.copy(
                        ads = result.ads,
                        isLoading = false
                    )
                }
                is AdResult.Failure -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                }
                else -> _state.value = _state.value.copy(isLoading = false)
            }
            _state.value = _state.value.copy(isLoading = false)
        }
    }
}