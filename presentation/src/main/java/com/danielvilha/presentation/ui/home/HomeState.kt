package com.danielvilha.presentation.ui.home

import com.danielvilha.model.data.Ad


data class HomeState(
    var ads: List<Ad> = emptyList<Ad>(),
    var isLoading: Boolean = false,
    val onAdClick: (String) -> Unit = {},
    val onCreateAd: () -> Unit = {},
    val onMenuClick: () -> Unit = {},
    val onNavigateToMyAds: () -> Unit = {},
    val onNavigateToUserEdit: () -> Unit = {},
    val onLogoutConfirmed: () -> Unit = {},
)
