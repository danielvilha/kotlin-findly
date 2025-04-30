package com.danielvilha.presentation.ui.ads

import com.danielvilha.model.data.Ad


data class AdsState(
    var ads: List<Ad> = emptyList<Ad>(),
    var isLoading: Boolean = false,
    var error: String? = null,

    val onBackPressed: () -> Unit = {},
    val onEditClick: (String) -> Unit = {},
    val onToggleVisibility: (Ad) -> Unit = {},
)
