package com.danielvilha.presentation.ui.register

import android.util.Patterns
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.danielvilha.presentation.R
import com.danielvilha.presentation.util.ExcludeFromJacocoGeneratedReport
import com.danielvilha.presentation.util.FindlyTopBar
import com.danielvilha.presentation.util.LightDarkPreview
import com.danielvilha.theme.FindlyTheme
import com.danielvilha.theme.custom.FindlyButton
import com.danielvilha.theme.custom.FindlyOutlinedTextField
import com.danielvilha.theme.robotoFontFamily

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun RegisterScreenPreview(
    @PreviewParameter(RegisterProvider::class)
    value: RegisterState
) {
    FindlyTheme {
        RegisterScreen(
            state = value,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(state: RegisterState) {
    var context = LocalContext.current
    val isEmailValid =
        state.email.length >= 4 && !Patterns.EMAIL_ADDRESS.matcher(state.email).matches()
    val isPasswordValid = state.password.isNotEmpty() && state.password.length < 6
    var passwordVisible by remember { mutableStateOf(false) }
    val isConfirmPasswordValid =
        state.confirmPassword.isNotEmpty() && state.confirmPassword != state.password
    var confirmPasswordVisible by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            FindlyTopBar(
                title = "Register",
                showBackButton = true,
                onBackClick = { state.onBackPressed() }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_findly),
                    contentDescription = "Findly logo",
                    modifier = Modifier.size(60.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(32.dp))

                FindlyOutlinedTextField(
                    value = state.email,
                    onValueChange = { state.onEmailChange(it) },
                    label = "Email",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = isEmailValid,
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = "Must be a valid email address",
                )

                FindlyOutlinedTextField(
                    value = state.password,
                    onValueChange = state.onPasswordChange,
                    label = "Password",
                    isError = isPasswordValid,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation =
                        if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (passwordVisible)
                            Icons.Default.VisibilityOff
                        else
                            Icons.Default.Visibility
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = icon, contentDescription = null)
                        }
                    },
                    supportingText = "Password must be at least 6 characters long",
                )

                FindlyOutlinedTextField(
                    value = state.confirmPassword,
                    onValueChange = state.onConfirmPasswordChange,
                    label = "Confirm Password",
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

                Spacer(modifier = Modifier.height(16.dp))
                FindlyButton(
                    onClick = {
                        if (isEmailValid && isPasswordValid && isConfirmPasswordValid) {
                            Toast.makeText(context, "Check the entered fields", Toast.LENGTH_LONG).show()
                        } else {
                            state.onRegister()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    text = "Register"
                )

                state.error?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it, color = Color.Red)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f))
                    Text(text = " or ", color = Color.Gray)
                    HorizontalDivider(modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { state.createUserWithGoogleInternal() },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_google),
                            contentDescription = "Login Google",
                            tint = Color.Unspecified
                        )
                    }
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
private class RegisterProvider : PreviewParameterProvider<RegisterState> {

    override val values: Sequence<RegisterState>
        get() = sequenceOf(
            RegisterState(
                email = "",
                password = "",
                confirmPassword = "",
                error = null,
                isLoading = false,
            ),
            RegisterState(
                email = "mail@mail.com",
                password = "12345678",
                confirmPassword = "12345678",
                error = null,
                isLoading = false,
            ),
            RegisterState(
                email = "mail#mail.com",
                password = "12345678",
                confirmPassword = "12345678",
                error = "Password invalid",
                isLoading = false,
            ),
            RegisterState(
                email = "mail@mail.com",
                password = "12345678",
                confirmPassword = "12345678",
                error = null,
                isLoading = true,
            ),
        )
}