package com.danielvilha.presentation.ui.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val error: String? = null,
    val isLoading: Boolean = false,
    val onEmailChange: (String) -> Unit = {},
    val onPasswordChange: (String) -> Unit = {},
    val onLogin: () -> Unit = {},
    val goToRegister: () -> Unit = {},
    val signInWithGoogle: () -> Unit = {},
)
