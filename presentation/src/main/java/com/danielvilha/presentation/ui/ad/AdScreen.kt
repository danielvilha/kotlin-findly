package com.danielvilha.presentation.ui.ad

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.danielvilha.model.data.Ad
import com.danielvilha.model.enum.AdMode
import com.danielvilha.model.enum.AdType
import com.danielvilha.model.enum.AdTypeOption
import com.danielvilha.presentation.util.ExcludeFromJacocoGeneratedReport
import com.danielvilha.presentation.util.FindlyTopBar
import com.danielvilha.presentation.util.LightDarkPreview
import com.danielvilha.presentation.util.PhoneNumberInput
import com.danielvilha.theme.FindlyTheme
import com.danielvilha.theme.custom.FindlyOutlinedTextField

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun AdScreenPreview(
    @PreviewParameter(AdProvider::class)
    value: AdState
) {
    FindlyTheme {
        AdScreen(
            state = value,
        )
    }
}

@Composable
fun AdScreen(state: AdState) {
    val isEditable = state.mode == AdMode.EDIT || state.mode == AdMode.CREATE
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        state.onAddImages(uris)
    }

    val adType = state.ad.type
    val showExtraFields = adType != null
    val showWorkFields = adType == AdType.WORK
    val showOtherFields = adType != AdType.WORK
    var showExitDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            FindlyTopBar(
                title = when(state.mode) {
                    AdMode.EDIT -> "Edit Ad"
                    AdMode.VIEW -> "View Ad"
                    else -> "Create Ad"
                }.toString(),
                showBackButton = true,
                onBackClick = {
                    if (state.isModifier) showExitDialog = true
                    else {
                        showExitDialog = false
                        state.onBackPressed()
                    }
                },
                action = {
                    if (isEditable) {
                        TextButton(onClick = state.onSave) {
                            Text(
                                text = "Save",
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            )
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                item {
                    if (state.error != null) {
                        AlertDialog(
                            onDismissRequest = state.onDismissDialog,
                            confirmButton = {
                                TextButton(onClick = state.onDismissDialog) {
                                    Text("OK")
                                }
                            },
                            title = { Text("Attention") },
                            text = { Text(state.error ?: "") }
                        )
                    }
                    AdTypeDropdown(
                        state = state,
                        selectedType = state.ad.type,
                        onTypeSelected = { type ->
                            if (type != null) state.onTypeChange(type)
                        },
                        readOnly = !isEditable
                    )
                    if (isEditable) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Is Visible?")
                            Spacer(modifier = Modifier.width(8.dp))
                            Switch(
                                checked = state.ad.visible == true,
                                onCheckedChange = state.onVisibleChange,
                                colors = SwitchDefaults.colors(
                                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                                )
                            )
                        }
                    }
                    FindlyOutlinedTextField(
                        value = state.ad.title,
                        onValueChange = state.onTitleChange,
                        label = "Title",
                        isError = state.validation.titleError != null,
                        readOnly = !isEditable,
                        modifier = Modifier.fillMaxWidth(),
                        supportingText = state.validation.titleError
                    )
                    FindlyOutlinedTextField(
                        value = state.ad.description,
                        onValueChange = state.onDescriptionChange,
                        label = "Description",
                        isError = state.validation.descriptionError != null,
                        readOnly = !isEditable,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 240.dp),
                        singleLine = false,
                        supportingText = state.validation.descriptionError
                    )

                    if (showExtraFields) {
                        PhoneNumberInput(
                            selectedCountryCode = state.selectedCountryCode,
                            onCountryCodeChange = state.onCountryCodeChange,
                            phoneNumber = state.ad.phone ?: "",
                            onPhoneNumberChange = state.onPhoneChange,
                            isEditable = isEditable,
                            isError = state.validation.phoneError != null,
                            errorText = state.validation.phoneError
                        )
                        FindlyOutlinedTextField(
                            value = state.ad.email ?: "",
                            onValueChange = state.onEmailChange,
                            label = "Email",
                            isError = state.validation.emailError != null,
                            readOnly = !isEditable,
                            modifier = Modifier.fillMaxWidth(),
                            supportingText = state.validation.emailError
                        )

                        if (showWorkFields) {
                            FindlyOutlinedTextField(
                                value = state.ad.address ?: "",
                                onValueChange = state.onAddressChange,
                                label = "Address",
                                isError = state.validation.addressError != null,
                                readOnly = !isEditable,
                                modifier = Modifier.fillMaxWidth(),
                                supportingText = state.validation.addressError
                            )
                            FindlyOutlinedTextField(
                                value = state.ad.url ?: "",
                                onValueChange = state.onUrlChange,
                                label = "URL",
                                isError = state.validation.urlError != null,
                                readOnly = !isEditable,
                                modifier = Modifier.fillMaxWidth(),
                                supportingText = state.validation.urlError
                            )
                        }

                        if (showOtherFields && isEditable) {
                            IconButton(onClick = { imagePickerLauncher.launch("image/*") }) {
                                Icon(Icons.Filled.AddAPhoto, contentDescription = "")
                            }
                        }

                        if (showOtherFields && state.ad.imageUrls.isNotEmpty()) {
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(state.ad.imageUrls.chunked(2)) { rowImages ->
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        rowImages.forEach { imageUrl ->
                                            Image(
                                                painter = rememberAsyncImagePainter(model = imageUrl),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .size(100.dp)
                                                    .aspectRatio(1f),
                                                contentScale = ContentScale.Crop
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (showExitDialog) {
                AlertDialog(
                    onDismissRequest = { showExitDialog = false },
                    title = { Text("Discard changes?") },
                    text = { Text("You have unsaved changes. Are you sure you want to go back?") },
                    confirmButton = {
                        TextButton(onClick = {
                            showExitDialog = false
                            state.onBackPressed()
                        }) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showExitDialog = false }) {
                            Text("No")
                        }
                    }
                )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdTypeDropdown(
    state: AdState,
    selectedType: AdType?,
    onTypeSelected: (AdType?) -> Unit,
    readOnly: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    val adTypeOptions = listOf(
        AdTypeOption("Select", null),
        AdTypeOption("Work", AdType.WORK),
        AdTypeOption("Home", AdType.HOME),
        AdTypeOption("Selling", AdType.SELLING),
        AdTypeOption("Renting", AdType.RENTING),
        AdTypeOption("Service", AdType.SERVICE)
    )

    val selectedLabel = adTypeOptions.find { it.type == selectedType }?.label ?: "Select"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        FindlyOutlinedTextField(
            value = selectedLabel,
            onValueChange = { onTypeSelected },
            label = "Ad types",
            isError = state.validation.typeError != null,
            readOnly = readOnly,
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, !readOnly)
                .fillMaxWidth(),
            supportingText = state.validation.typeError,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            adTypeOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label) },
                    onClick = {
                        onTypeSelected(option.type)
                        expanded = false
                    }
                )
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
private class AdProvider : PreviewParameterProvider<AdState> {

    override val values: Sequence<AdState>
        get() = sequenceOf(
            AdState(
                ad = Ad(),
                mode = AdMode.CREATE,
                error = null,
            ),
            AdState(
                ad = Ad(
                    id = "123456",
                    userId = "1234567890",
                    type = AdType.HOME,
                    title = "I'm looking for a bedroom",
                    description = "I'm looking for a bedroom in Dublin",
                    phone = "083 123 4567",
                    email = "name@mail.ie",
                    address = "Area",
                    url = null,
                    imageUrls = listOf(
                        "https://ca-times.brightspotcdn.com/dims4/default/c63ea98/2147483647/strip/true/crop/5170x3447+0+0/resize/1200x800!/format/webp/quality/75/?url=https%3A%2F%2Fcalifornia-times-brightspot.s3.amazonaws.com%2Fbe%2F98%2F5905643b48caa50a7db7702fe2ee%2Fbritain-star-wars-the-last-jedi-premiere-18836.jpg",
                        "https://ca-times.brightspotcdn.com/dims4/default/c63ea98/2147483647/strip/true/crop/5170x3447+0+0/resize/1200x800!/format/webp/quality/75/?url=https%3A%2F%2Fcalifornia-times-brightspot.s3.amazonaws.com%2Fbe%2F98%2F5905643b48caa50a7db7702fe2ee%2Fbritain-star-wars-the-last-jedi-premiere-18836.jpg",
                        "https://ca-times.brightspotcdn.com/dims4/default/c63ea98/2147483647/strip/true/crop/5170x3447+0+0/resize/1200x800!/format/webp/quality/75/?url=https%3A%2F%2Fcalifornia-times-brightspot.s3.amazonaws.com%2Fbe%2F98%2F5905643b48caa50a7db7702fe2ee%2Fbritain-star-wars-the-last-jedi-premiere-18836.jpg",
                    ),
                    visible = true,
                    expirationDate = "",
                ),
                mode = AdMode.CREATE,
                error = null,
            ),
        )
}
