package com.danielvilha.model.data

import com.danielvilha.model.enum.AdType
import com.danielvilha.model.CountryCode

data class Ad(
    var id: String? = null,
    var userId: String? = null,
    var title: String = "",
    var description: String = "",
    var phone: String? = null,
    var countryCode: CountryCode? = CountryCode(),
    var email: String? = null,
    var address: String? = null,
    var url: String? = null,
    val imageUrls: List<String> = emptyList(),
    var type: AdType? = null,
    val isResolved: Boolean = false,
    var expirationDate: String? = null,
    var visible: Boolean = true
)