package com.danielvilha.model.enum

enum class AdType {
    WORK, HOME, SELLING, RENTING, SERVICE
}

data class AdTypeOption(val label: String, val type: AdType?)