package com.danielvilha.findly.ui.register

import androidx.lifecycle.ViewModel
import com.danielvilha.presentation.ui.register.RegisterState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state

    private var onNavigate: ((String) -> Unit)? = null

    init {
        _state.value = _state.value.copy(
            onEmailChange = ::onEmailChange,
            onPasswordChange = ::onPasswordChange,
            onConfirmPasswordChange = ::onConfirmPasswordChange,
            onRegister = { registerInternal() },
            onBackPressed = ::onBackPressed,
            createUserWithGoogleInternal = { createUserWithGoogleInternal() },
        )
    }

    fun setNavigationCallback(callback: (String) -> Unit) {
        onNavigate = callback
    }

    fun onEmailChange(newEmail: String) {
        _state.value = _state.value.copy(email = newEmail)
    }

    fun onPasswordChange(newPassword: String) {
        _state.value = _state.value.copy(password = newPassword)
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _state.value = _state.value.copy(confirmPassword = newConfirmPassword)
    }

    private fun registerInternal() {
        _state.value = _state.value.copy(isLoading = true)
        if (_state.value.password == _state.value.confirmPassword) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(_state.value.email, _state.value.password)
                .addOnSuccessListener {
                    _state.value = _state.value.copy(isLoading = false)
                    onNavigate?.invoke("homeFragment")
                }
                .addOnFailureListener {
                    _state.value = _state.value.copy(isLoading = false)
                    _state.value.copy(error = it.message)
                }
        } else {
            _state.value = _state.value.copy(isLoading = false)
            _state.value.copy(error = "The password and the confirmation does not match")
        }
    }

    private fun createUserWithGoogleInternal() {
        onNavigate?.invoke("onRegisterGoogleSignIn")
    }

    private fun onBackPressed() {
        onNavigate?.invoke("onBackPressed")
    }
}