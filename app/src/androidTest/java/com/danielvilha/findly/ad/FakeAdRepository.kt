package com.danielvilha.findly.ad

import com.danielvilha.model.data.Ad
import com.danielvilha.model.repository.AdRepository
import com.danielvilha.model.repository.AdResult

class FakeAdRepository : AdRepository {

    private val ads = mutableListOf<Ad>()

    override suspend fun loadAdById(id: String): AdResult {
        val ad = ads.find { it.id == id }
        return ad?.let { AdResult.Success(it) } ?: AdResult.Failure("Ad not found")
    }

    override suspend fun createOrUpdateAd(ad: Ad): AdResult {
        val existingIndex = ads.indexOfFirst { it.id == ad.id }
        return if (existingIndex != -1) {
            ads[existingIndex] = ad
            AdResult.Success(ad)
        } else {
            ads.add(ad.copy(id = ad.id?.ifBlank { "fake-id-${ads.size}" }))
            AdResult.Success(ad)
        }
    }

    override suspend fun loadMyAds(userId: String): AdResult {
        val myAds = ads.filter { it.userId == userId }
        return AdResult.SuccessList(myAds)
    }

    override suspend fun loadAds(): AdResult {
        val visibleAds = ads.filter { it.visible }
        return AdResult.SuccessList(visibleAds)
    }
}