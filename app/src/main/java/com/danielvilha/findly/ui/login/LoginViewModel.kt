package com.danielvilha.findly.ui.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielvilha.findly.BuildConfig
import com.danielvilha.presentation.ui.login.LoginState
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LoginViewModel"

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    private var onNavigate: ((String) -> Unit)? = null

    init{
        _state.value = _state.value.copy(
            onEmailChange = ::onEmailChange,
            onPasswordChange = ::onPasswordChange,
            onLogin = ::loginInternal,
            goToRegister = ::goToRegister,
            signInWithGoogle = ::signInWithGoogle
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

    private fun signInWithGoogle() {
        onNavigate?.invoke("signInWithGoogleInternal")
    }

    fun signInWithGoogleInternal(context: Context) {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                    .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()
                val credentialManager = CredentialManager.create(context)
                val result = credentialManager.getCredential(
                    request = request,
                    context = context
                )

                val credential = result.credential
                when (credential) {
                    is PublicKeyCredential -> {
                        val authentication = credential.authenticationResponseJson
                        authentication
                    }

                    is PasswordCredential -> {
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                            credential.id,
                            credential.password,
                        )
                            .addOnSuccessListener {
                                _state.value = _state.value.copy(isLoading = false)
                                onNavigate?.invoke("homeFragment")
                            }
                            .addOnFailureListener {
                                _state.value = _state.value.copy(isLoading = false)
                                _state.value = _state.value.copy(error = it.message)
                            }
                            .addOnCompleteListener {
                                _state.value = _state.value.copy(isLoading = false)
                            }
                    }

                    // GoogleIdToken credential
                    is CustomCredential -> {
                        if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                            try {
                                val googleIdTokenCredential = GoogleIdTokenCredential
                                    .createFrom(credential.data)
                                googleIdTokenCredential
                                // pass googleIdTokenCredential.getIdToken() to the backend server.
                                // GoogleIdToken idToken = verifier.verify(idTokenString);
                                // use the subject ID: idToken.getPayload().getSubject()
                            } catch (e: GoogleIdTokenParsingException) {
                                _state.value = _state.value.copy(isLoading = false)
                                Log.e(TAG, "Received an invalid google id token response", e)
                            }
                        } else {
                            _state.value = _state.value.copy(isLoading = false)
                            Log.e(TAG, "Unexpected type of credential")
                        }
                    }
                }
            } catch (e: GetCredentialException) {
                if (e is NoCredentialException) {
                    Toast
                        .makeText(
                            context,
                            "No saved credentials found. Please sign in or create an account.",
                            Toast.LENGTH_LONG
                        ).show()
                    Log.e("NoCredentialException", "Error getting credentials", e)
                } else {
                    Toast
                        .makeText(
                            context,
                            "Error retrieving credentials: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    Log.e("CredentialError", "Error getting credentials", e)
                }
                _state.value = _state.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun loginInternal() {
        _state.value = _state.value.copy(isLoading = true)
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            _state.value.email,
            _state.value.password,
        )
            .addOnSuccessListener {
                _state.value = _state.value.copy(isLoading = false)
                onNavigate?.invoke("homeFragment")
            }
            .addOnFailureListener {
                _state.value = _state.value.copy(isLoading = false)
                _state.value = _state.value.copy(error = it.message)
            }
            .addOnCompleteListener {
                _state.value = _state.value.copy(isLoading = false)
            }
    }

    private fun goToRegister() {
        onNavigate?.invoke("registerFragment")
    }
}