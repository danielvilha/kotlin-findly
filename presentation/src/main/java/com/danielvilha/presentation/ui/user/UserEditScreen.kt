package com.danielvilha.presentation.ui.user

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.danielvilha.presentation.util.ExcludeFromJacocoGeneratedReport
import com.danielvilha.presentation.util.FindlyTopBar
import com.danielvilha.presentation.util.LightDarkPreview
import com.danielvilha.theme.FindlyTheme
import com.danielvilha.theme.custom.FindlyOutlinedTextField


@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun UserEditPrivate(
    @PreviewParameter(UserEditProvider::class)
    value: UserEditState
) {
    FindlyTheme {
        UserEditScreen(state = value)
    }
}

@Composable
fun UserEditScreen(state: UserEditState) {
    val isOldPasswordValid = state.oldPassword.isNotEmpty() && state.oldPassword.length < 6
    val isNewPasswordValid = state.newPassword.isNotEmpty() && state.newPassword.length < 6
    val isConfirmPasswordValid =
        state.confirmPassword.isNotEmpty() && state.confirmPassword != state.newPassword
    var oldPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            FindlyTopBar(
                title = "Edit User",
                showBackButton = true,
                onBackClick = state.onBackPressed,
                action = {
                    TextButton(onClick = state.onPasswordReset) {
                        Text(
                            text = "Save",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    if (state.message != null) {
                        AlertDialog(
                            onDismissRequest = state.onDismissDialog,
                            confirmButton = {
                                TextButton(onClick = state.onDismissDialog) {
                                    Text(text = "OK")
                                }
                            },
                            title = { Text(text = "Attention") },
                            text = { Text(text = state.message) }
                        )
                    }
                    FindlyOutlinedTextField(
                        value = state.email,
                        onValueChange = {},
                        label = "Email",
                        readOnly = false,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                item {
                    FindlyOutlinedTextField(
                        value = state.oldPassword,
                        onValueChange = state.onOldPasswordChanged,
                        label = "Old password",
                        isError = isOldPasswordValid,
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation =
                            if (oldPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val icon = if (oldPasswordVisible)
                                Icons.Default.VisibilityOff
                            else
                                Icons.Default.Visibility
                            IconButton(onClick = { oldPasswordVisible = !oldPasswordVisible }) {
                                Icon(imageVector = icon, contentDescription = null)
                            }
                        },
                        supportingText = "Password must be at least 6 characters long",
                    )
                }
                item {
                    FindlyOutlinedTextField(
                        value = state.newPassword,
                        onValueChange = state.onNewPasswordChanged,
                        label = "New password",
                        isError = isNewPasswordValid,
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation =
                            if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val icon = if (newPasswordVisible)
                                Icons.Default.VisibilityOff
                            else
                                Icons.Default.Visibility
                            IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                                Icon(imageVector = icon, contentDescription = null)
                            }
                        },
                        supportingText = "Password must be at least 6 characters long",
                    )
                }
                item {
                    FindlyOutlinedTextField(
                        value = state.confirmPassword,
                        onValueChange = state.onConfirmPasswordChanged,
                        label = "Confirm password",
                        isError = isConfirmPasswordValid,
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation =
                            if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val icon = if (confirmPasswordVisible)
                                Icons.Default.VisibilityOff
                            else
                                Icons.Default.Visibility
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(imageVector = icon, contentDescription = null)
                            }
                        },
                        supportingText = "Passwords do not match",
                    )
                }
            }

            AnimatedVisibility(
                visible = state.isLoading,
                modifier = Modifier.fillMaxSize(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    )
}

@ExcludeFromJacocoGeneratedReport
private class UserEditProvider : PreviewParameterProvider<UserEditState> {

    override val values: Sequence<UserEditState>
        get() = sequenceOf(
            UserEditState(
                email = "test@test.com",
                oldPassword = "Test",
                newPassword = "Test1",
                message = null,
            ),
            UserEditState(
                email = "test@test.com",
                oldPassword = "Test",
                newPassword = "Test1",
                message = null,
                isLoading = true
            )
        )
}