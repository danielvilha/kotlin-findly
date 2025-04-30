package com.danielvilha.model.data

data class FieldValidation(
    val titleError: String? = null,
    val descriptionError: String? = null,
    val phoneError: String? = null,
    val emailError: String? = null,
    val addressError: String? = null,
    val photosError: String? = null,
    val urlError: String? = null,
    val typeError: String? = null,
)