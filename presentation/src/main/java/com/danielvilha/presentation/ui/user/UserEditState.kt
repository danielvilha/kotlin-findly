package com.danielvilha.presentation.ui.user

data class UserEditState(
    val email: String = "",
    val oldPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val message: String? = null,
    val isLoading: Boolean = false,

    val onBackPressed: () -> Unit = {},
    val onPasswordReset: () -> Unit = {},
    val onDismissDialog: () -> Unit = {},
    val onOldPasswordChanged: (String) -> Unit = {},
    val onNewPasswordChanged: (String) -> Unit = {},
    val onConfirmPasswordChanged: (String) -> Unit = {},
)
