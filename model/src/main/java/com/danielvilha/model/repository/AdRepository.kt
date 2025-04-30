package com.danielvilha.model.repository

import com.danielvilha.model.data.Ad

interface AdRepository {
    suspend fun loadAdById(id: String): AdResult
    suspend fun createOrUpdateAd(ad: Ad): AdResult
    suspend fun loadMyAds(userId: String): AdResult
    suspend fun loadAds(): AdResult
}