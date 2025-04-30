package com.danielvilha.model.repository

import com.danielvilha.model.data.Ad

sealed class AdResult {
    data class Success(val ad: Ad) : AdResult()
    data class SuccessList(val ads: List<Ad>) : AdResult()
    data class Failure(val message: String) : AdResult()
}
