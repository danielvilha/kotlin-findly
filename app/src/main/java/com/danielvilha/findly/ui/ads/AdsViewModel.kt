package com.danielvilha.findly.ui.ads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielvilha.model.repository.AdRepositoryImpl
import com.danielvilha.model.repository.AdResult
import com.danielvilha.presentation.ui.ads.AdsState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdsViewModel @Inject constructor(
    private val repository: AdRepositoryImpl
) : ViewModel() {
    private val _state = MutableStateFlow(AdsState())
    val state: StateFlow<AdsState> = _state

    private var onNavigate: ((String) -> Unit)? = null

    init {
        _state.value = _state.value.copy(
            onBackPressed = ::onBackPressed,
            onEditClick = ::onEditClick
        )
        loadUserAds()
    }

    fun setNavigationCallback(callback: (String) -> Unit) {
        onNavigate = callback
    }

    private fun onBackPressed() {
        onNavigate?.invoke("onBackPressed")
    }

    private fun onEditClick(id: String) {
        onNavigate?.invoke("onEditClick/$id")
    }

    private fun loadUserAds() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            when (val result = repository.loadMyAds(userId)) {
                is AdResult.SuccessList -> {
                    _state.value = _state.value.copy(
                        ads = result.ads,
                        isLoading = false
                    )
                }
                is AdResult.Failure -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                else -> _state.value = _state.value.copy(isLoading = false)
            }
        }
    }
}