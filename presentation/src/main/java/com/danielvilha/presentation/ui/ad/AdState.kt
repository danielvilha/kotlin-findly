package com.danielvilha.presentation.ui.ad

import android.net.Uri
import com.danielvilha.model.Ad
import com.danielvilha.model.AdMode
import com.danielvilha.model.AdType
import com.danielvilha.model.CountryCode
import com.danielvilha.model.CountryCodeList
import com.danielvilha.model.FieldValidation

data class AdState(
    var ad: Ad = Ad(),
    var mode: AdMode? = null,
    var error: String? = null,
    var isLoading: Boolean = false,
    var isModifier: Boolean = false,
    var selectedCountryCode: CountryCode = CountryCodeList.all.first(),
    val onCountryCodeChange: (CountryCode) -> Unit = {},
    var validation: FieldValidation = FieldValidation(),
    val onDismissDialog: () -> Unit = {},
    val onTypeChange: (AdType?) -> Unit = {},
    val onTitleChange: (String) -> Unit = {},
    val onDescriptionChange: (String) -> Unit = {},
    val onPhoneChange: (String) -> Unit = {},
    val onEmailChange: (String) -> Unit = {},
    val onAddressChange: (String) -> Unit = {},
    val onUrlChange: (String) -> Unit = {},
    val onImageUrlsChange: (List<String>) -> Unit = {},
    val onVisibleChange: (Boolean) -> Unit = {},
    val onExpirationDateChange: (String) -> Unit = {},
    val onSave: () -> Unit = {},
    val onBackPressed: () -> Unit = {},
    val onAddImages: (List<Uri>) -> Unit = {},
)
