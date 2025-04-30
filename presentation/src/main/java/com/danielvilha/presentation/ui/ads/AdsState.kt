package com.danielvilha.presentation.ui.ads

import com.danielvilha.model.Ad


data class AdsState(
    var ads: List<Ad> = emptyList<Ad>(),
    var isLoading: Boolean = false,
    var error: String? = null,

    val onBackPressed: () -> Unit = {},
    val onEditClick: (Ad) -> Unit = {},
    val onToggleVisibility: (Ad) -> Unit = {},
)
