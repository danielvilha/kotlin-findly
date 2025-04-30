package com.danielvilha.model

data class CountryCode(
    val name: String = "",
    val code: String = "",
    val flag: String = ""
)

object CountryCodeList {
    val all = listOf(
        CountryCode("United States", "+1", "\uD83C\uDDFA\uD83C\uDDF8"),
        CountryCode("Brazil", "+55", "\uD83C\uDDE7\uD83C\uDDF7"),
        CountryCode("United Kingdom", "+44", "\uD83C\uDDEC\uD83C\uDDE7"),
        CountryCode("Ireland", "+353", "\uD83C\uDDEE\uD83C\uDDEA"),
        CountryCode("Germany", "+49", "\uD83C\uDDE9\uD83C\uDDEA"),
        CountryCode("France", "+33", "\uD83C\uDDEB\uD83C\uDDF7"),
        CountryCode("Spain", "+34", "\uD83C\uDDEA\uD83C\uDDF8"),
        CountryCode("Italy", "+39", "\uD83C\uDDEE\uD83C\uDDF9"),
        CountryCode("Canada", "+1", "\uD83C\uDDE8\uD83C\uDDE6"),
        CountryCode("Portugal", "+351", "\uD83C\uDDF5\uD83C\uDDF9"),
        CountryCode("India", "+91", "\uD83C\uDDEE\uD83C\uDDF3"),
        CountryCode("Japan", "+81", "\uD83C\uDDEF\uD83C\uDDF5"),
        CountryCode("Australia", "+61", "\uD83C\uDDE6\uD83C\uDDFA"),
        CountryCode("Argentina", "+54", "\uD83C\uDDE6\uD83C\uDDF7"),
        CountryCode("Mexico", "+52", "\uD83C\uDDF2\uD83C\uDDFD"),
        CountryCode("Netherlands", "+31", "\uD83C\uDDF3\uD83C\uDDF1"),
        CountryCode("Belgium", "+32", "\uD83C\uDDE7\uD83C\uDDEA"),
        CountryCode("Switzerland", "+41", "\uD83C\uDDE8\uD83C\uDDED"),
        CountryCode("Norway", "+47", "\uD83C\uDDF3\uD83C\uDDF4"),
        CountryCode("Sweden", "+46", "\uD83C\uDDF8\uD83C\uDDEA"),
        CountryCode("Denmark", "+45", "\uD83C\uDDE9\uD83C\uDDF0"),
        CountryCode("Finland", "+358", "\uD83C\uDDEB\uD83C\uDDEE"),
        CountryCode("China", "+86", "\uD83C\uDDE8\uD83C\uDDF3"),
        CountryCode("Russia", "+7", "\uD83C\uDDF7\uD83C\uDDFA"),
        CountryCode("South Africa", "+27", "\uD83C\uDDFF\uD83C\uDDE6")
    )
}
