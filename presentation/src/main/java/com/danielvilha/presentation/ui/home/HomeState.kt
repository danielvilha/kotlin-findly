package com.danielvilha.presentation.ui.home

import com.danielvilha.model.Ad


data class HomeState(
    var ads: List<Ad> = emptyList<Ad>(),
    var isLoading: Boolean = false,
    val onAdClick: (String) -> Unit = {},
    val onCreateAd: () -> Unit = {},
    val onMenuClick: () -> Unit = {},
    val onNavigateToMyAds: () -> Unit = {},
    val onLogoutConfirmed: () -> Unit = {},
)
