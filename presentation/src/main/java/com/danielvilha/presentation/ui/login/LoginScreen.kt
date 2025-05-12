package com.danielvilha.presentation.ui.login

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.danielvilha.presentation.util.LightDarkPreview
import com.danielvilha.theme.FindlyTheme
import com.danielvilha.theme.custom.FindlyButton
import com.danielvilha.theme.custom.FindlyOutlinedTextField
import com.danielvilha.theme.robotoFontFamily

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun LoginScreenPreview(
    @PreviewParameter(LoginProvider::class)
    value: LoginState
) {
    FindlyTheme {
        LoginScreen(
            state = value
        )
    }
}

@Composable
fun LoginScreen(state: LoginState) {
    val context = LocalContext.current
    var passwordVisible by remember { mutableStateOf(false) }
    val isEmailValid = state.email.length >= 4 && !Patterns.EMAIL_ADDRESS.matcher(state.email).matches()
    val isPasswordValid = state.password.isNotEmpty() && state.password.length < 6

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
                fontWeight = FontWeight.Bold,
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
            supportingText = "Must be a valid email address"
        )

        FindlyOutlinedTextField(
            value = state.password,
            onValueChange = { state.onPasswordChange(it) },
            label = "Password",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = isPasswordValid,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            supportingText = "Password must be at least 6 characters long",
            trailingIcon = {
                val icon = if (passwordVisible)
                    Icons.Default.VisibilityOff
                else
                    Icons.Default.Visibility
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            },
        )

        Spacer(modifier = Modifier.height(16.dp))
        FindlyButton(
            onClick = {
                if (state.email.isBlank() || state.password.isBlank()) {
                    Toast
                        .makeText(context, "Email and password are required", Toast.LENGTH_LONG)
                        .show()
                } else {
                    state.onLogin()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            text = "Enter"
        )

        state.error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = { state.goToRegister() }) {
            Text("Don't have an account? Create an account now")
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

        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = { state.signInWithGoogle() },
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

@ExcludeFromJacocoGeneratedReport
private class LoginProvider: PreviewParameterProvider<LoginState> {

    override val values: Sequence<LoginState>
        get() = sequenceOf(
            LoginState(
                email = "",
                password = "",
                error = null,
                isLoading = false,
            ),
            LoginState(
                email = "test@example.com",
                password = "123456",
                error = null,
                isLoading = false,
            ),
            LoginState(
                email = "test@example.com",
                password = "123456",
                error = null,
                isLoading = true,
            ),
            LoginState(
                email = "test@example.com",
                password = "123456",
                error = "Incorrect username or password",
                isLoading = false,
            ),
        )
}