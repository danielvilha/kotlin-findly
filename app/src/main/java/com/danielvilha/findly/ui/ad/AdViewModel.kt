package com.danielvilha.findly.ui.ad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielvilha.model.Ad
import com.danielvilha.presentation.ui.ad.AdState
import com.danielvilha.model.AdType
import com.danielvilha.model.CountryCode
import com.danielvilha.model.CountryCodeList
import com.danielvilha.model.FieldValidation
import com.danielvilha.model.repository.AdRepository
import com.danielvilha.model.repository.AdResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import kotlin.String

@HiltViewModel
class AdViewModel @Inject constructor(
    private val repository: AdRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AdState())
    val state: StateFlow<AdState> = _state

    private var onNavigate: ((String) -> Unit)? = null

    private val countryCodes = CountryCodeList.all
    private val defaultCountry =
        countryCodes.find { it.name == Locale.getDefault().country } ?: countryCodes.first()

    init {
        _state.value = _state.value.copy(
            onTypeChange = ::onTypeChange,
            onTitleChange = ::onTitleChange,
            onDescriptionChange = ::onDescriptionChange,
            onPhoneChange = ::onPhoneChange,
            onEmailChange = ::onEmailChange,
            onAddressChange = ::onAddressChange,
            onUrlChange = ::onUrlChange,
            onImageUrlsChange = {},
            onVisibleChange = ::onVisibleChange,
            onExpirationDateChange = ::onExpirationDateChange,
            onSave = ::saveAd,
            onBackPressed = ::onBackPressed,
            onAddImages = {},
            onDismissDialog = ::dismissDialog,
            selectedCountryCode = defaultCountry,
            onCountryCodeChange = ::onCountryCodeChange,
        )
    }

    fun setNavigationCallback(callback: (String) -> Unit) {
        onNavigate = callback
    }

    private fun updateAd(update: (Ad) -> Ad) {
        _state.value = _state.value.copy(ad = update(_state.value.ad))
    }

    private fun isModifier() {
        _state.value = _state.value.copy(isModifier = true)
    }

    fun onIdChange(newId: String?) = updateAd { it.copy(id = newId ?: "") }
    fun onUserIdChange(newUserId: String?) = updateAd { it.copy(userId = newUserId) }
    fun dismissDialog() {
        isModifier()
        _state.value = _state.value.copy(error = null)
    }
    private fun onTypeChange(newType: AdType?) {
        isModifier()
        updateAd { it.copy(type = newType ?: AdType.HOME) }
    }
    private fun onTitleChange(newTitle: String) {
        isModifier()
        updateAd { it.copy(title = newTitle) }
    }
    private fun onDescriptionChange(newDescription: String) {
        isModifier()
        updateAd { it.copy(description = newDescription) }
    }
    private fun onPhoneChange(newPhone: String) {
        isModifier()
        updateAd { it.copy(phone = newPhone) }
    }
    private fun onEmailChange(newEmail: String) {
        isModifier()
        updateAd { it.copy(email = newEmail) }
    }
    private fun onAddressChange(newAddress: String) {
        isModifier()
        updateAd { it.copy(address = newAddress) }
    }
    private fun onUrlChange(newUrl: String) {
        isModifier()
        updateAd { it.copy(url = newUrl) }
    }
    private fun onVisibleChange(newVisible: Boolean) {
        isModifier()
        updateAd { it.copy(visible = newVisible) }
    }
    private fun onExpirationDateChange(newExpirationDate: String) {
        isModifier()
        updateAd { it.copy(expirationDate = newExpirationDate) }
    }

    private fun onCountryCodeChange(newCountryCode: CountryCode) {
        isModifier()
        updateAd { it.copy(countryCode = newCountryCode) }
        _state.value.selectedCountryCode = newCountryCode
    }

    private fun isValid() = with(_state.value.ad) {
        userId?.isNotBlank() == true &&
                title.isNotBlank() &&
                description.isNotBlank() &&
                phone?.isNotBlank() == true &&
                email?.isNotBlank() == true &&
                !type?.name.equals("Select")
    }

    private fun validate(): Boolean {
        val ad = _state.value.ad
        val errors = FieldValidation(
            titleError = if (ad.title.isBlank()) "Mandatory title" else null,
            descriptionError = if (ad.description.isBlank()) "Mandatory description" else null,
            phoneError = if (ad.type == AdType.WORK && ad.phone.isNullOrBlank()) "Mandatory phone" else null,
            emailError = if (ad.type == AdType.WORK && ad.email.isNullOrBlank()) "Mandatory email" else null,
            addressError = if (ad.type == AdType.WORK && ad.address.isNullOrBlank()) "Mandatory address" else null,
            urlError = if (ad.type == AdType.WORK && ad.url.isNullOrBlank()) "Mandatory url" else null,
            typeError = if (ad.type?.name.equals("Select")) "Please, select one type" else null,
        )

        _state.value = _state.value.copy(validation = errors)
        return listOfNotNull(
            errors.titleError,
            errors.descriptionError,
            errors.phoneError,
            errors.emailError,
            errors.addressError,
            errors.photosError
        ).isEmpty()
    }

    private fun onBackPressed() {
        _state.value = _state.value.copy(isModifier = false)
        onNavigate?.invoke("onBackPressed")
    }

    fun loadAd(id: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            when (val result = repository.loadAdById(id)) {
                is AdResult.Success -> {
                    _state.value = _state.value.copy(
                        ad = result.ad,
                        isLoading = false
                    )
                }

                is AdResult.Failure -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                else -> _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    fun saveAd() {
        if (!validate()) return
        viewModelScope.launch {
            if (isValid()) {
                _state.value = _state.value.copy(isLoading = true)
                when (val result = repository.createOrUpdateAd(_state.value.ad)) {
                    is AdResult.Success -> {
                        _state.value = _state.value.copy(
                            ad = result.ad,
                            isLoading = false
                        )
                        onBackPressed()
                    }
                    is AdResult.Failure -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = result.message,
                        )
                    }
                    else -> _state.value = _state.value.copy(isLoading = false)
                }
            } else {
                _state.value = _state.value.copy(error = "Please fill in all required items.")
            }
        }
    }
}