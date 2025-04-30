package com.danielvilha.presentation.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.danielvilha.model.CountryCode
import com.danielvilha.model.CountryCodeList
import com.danielvilha.theme.custom.FindlyOutlinedTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneNumberInput(
    selectedCountryCode: CountryCode,
    onCountryCodeChange: (CountryCode) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    isEditable: Boolean,
    isError: Boolean,
    errorText: String?
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            FindlyOutlinedTextField(
                value = "${selectedCountryCode.flag} ${selectedCountryCode.code}",
                onValueChange = {},
                label = "",
                readOnly = true,
                modifier = Modifier
                    .width(130.dp)
                    .menuAnchor(MenuAnchorType.PrimaryEditable, isEditable),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                CountryCodeList.all.forEach { country ->
                    DropdownMenuItem(
                        text = { Text("${country.flag} ${country.code}") },
                        onClick = {
                            onCountryCodeChange(country)
                            expanded = false
                        }
                    )
                }
            }
        }

        FindlyOutlinedTextField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            label = "Phone",
            isError = isError,
            readOnly = !isEditable,
            supportingText = errorText,
            modifier = Modifier.weight(1f)
        )
    }
}
