package com.danielvilha.findly.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielvilha.presentation.ui.user.UserEditState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserEditViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(UserEditState())
    val state: StateFlow<UserEditState> = _state

    private var onNavigate: ((String) -> Unit)? = null

    private val _firebase = FirebaseAuth.getInstance()

    init {
        _state.value = _state.value.copy(
            email = _firebase.currentUser?.email ?: "Unknown",
            onPasswordReset = ::onPasswordReset,
            onBackPressed = ::onBackPressed,
            onDismissDialog = ::onDismissDialog,
            onOldPasswordChanged = ::onOldPasswordChange,
            onNewPasswordChanged = ::onNewPasswordChange,
            onConfirmPasswordChanged = ::onConfirmPasswordChange,
        )
    }

    fun setNavigationCallback(callback: (String) -> Unit) {
        onNavigate = callback
    }

    fun onOldPasswordChange(oldPassword: String) {
        _state.value = _state.value.copy(oldPassword = oldPassword)
    }

    fun onNewPasswordChange(newPassword: String) {
        _state.value = _state.value.copy(newPassword = newPassword)
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _state.value = _state.value.copy(confirmPassword = confirmPassword)
    }

    private fun onDismissDialog(message: String? = null) {
        _state.value = _state.value.copy(message = message)
    }

    private fun onPasswordReset() {
        if (_state.value.oldPassword.isEmpty()) {
            onDismissDialog(message = "Empty Old Passwords")
            return
        }
        if (_state.value.newPassword != _state.value.confirmPassword) {
            onDismissDialog(message = "New password and password confirmation do not match")
            return
        }
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            _firebase.confirmPasswordReset(_state.value.oldPassword, _state.value.newPassword)
                .addOnSuccessListener {
                    _state.value = _state.value.copy(isLoading = false)
                    onDismissDialog(message = "Password changed successfully")
                }
                .addOnFailureListener {
                    _state.value = _state.value.copy(isLoading = false)
                    onDismissDialog(message = it.message ?: "Unable to change your password")
                }
        }
    }

    private fun onBackPressed() {
        onNavigate?.invoke("onBackPressed")
    }
}