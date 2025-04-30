package com.danielvilha.presentation.ui.register

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val error: String? = null,
    val isLoading: Boolean = false,
    val onEmailChange: (String) -> Unit = {},
    val onPasswordChange: (String) -> Unit = {},
    val onConfirmPasswordChange: (String) -> Unit = {},
    val onRegister: () -> Unit = {},
    val onBackPressed: () -> Unit = {},
    val createUserWithGoogleInternal: () -> Unit = {},
)
