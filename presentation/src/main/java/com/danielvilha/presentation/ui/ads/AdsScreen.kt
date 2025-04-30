package com.danielvilha.presentation.ui.ads

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.danielvilha.model.Ad
import com.danielvilha.model.AdType
import com.danielvilha.presentation.util.ExcludeFromJacocoGeneratedReport
import com.danielvilha.presentation.util.FindlyTopBar
import com.danielvilha.presentation.util.LightDarkPreview
import com.danielvilha.theme.FindlyTheme

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun AdsScreenPreview(
    @PreviewParameter(AdsProvider::class)
    value : AdsState
) {
    FindlyTheme {
        AdsScreen(state = value)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdsScreen(state: AdsState) {
    Scaffold(
        topBar = {
            FindlyTopBar(
                title = "Ads",
                showBackButton = true,
                onBackClick = { state.onBackPressed() }
            )
        },
        content = { innerPadding ->
            if (state.ads.isEmpty() == true) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No ads available.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.ads) { ad ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = ad.title, style = MaterialTheme.typography.titleLarge)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = ad.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Type: ${ad.type?.name}", style = MaterialTheme.typography.bodySmall)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (ad.visible) "Status: Active" else "Status: Inactive",
                                    color = if (ad.visible) MaterialTheme.colorScheme.primary else Color.Gray,
                                    style = MaterialTheme.typography.labelSmall
                                )
                                ad.expirationDate?.let {
                                    Text(text = "Expires: $it", style = MaterialTheme.typography.labelSmall)
                                }
                                if (ad.isResolved) {
                                    Text(text = "✅ Resolved", color = MaterialTheme.colorScheme.primary)
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                    TextButton(onClick = { state.onEditClick(ad) }) {
                                        Text(text = "Edit")
                                    }
                                    TextButton(onClick = { state.onToggleVisibility(ad) }) {
                                        Text(text = if (ad.visible) "Deactivate" else "Activate")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = state.isLoading,
                modifier = Modifier.fillMaxSize(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    )
}

@ExcludeFromJacocoGeneratedReport
private class AdsProvider : PreviewParameterProvider<AdsState> {

    override val values: Sequence<AdsState>
        get() = sequenceOf(
            AdsState(
                ads = emptyList(),
                isLoading = false,
                error = null,
            ),
            AdsState(
                ads = emptyList(),
                isLoading = true,
                error = null,
            ),
            AdsState(
                ads = listOf(
                    Ad(
                        id = "123",
                        userId = "12345678",
                        title = "Bacon Ipsum",
                        description = "Bacon ipsum dolor amet pork chop beef capicola pastrami bresaola ball tip burgdoggen. Boudin short loin ham salami turkey jowl alcatra pork chop kevin turducken tenderloin tail. Shoulder ball tip beef ham hock alcatra fatback. Hamburger cupim kevin tongue, pork doner porchetta flank. Tongue shoulder fatback capicola ribeye pork loin. Jowl pancetta sausage, strip steak meatball capicola burgdoggen turkey. Short ribs shank spare ribs capicola biltong, chicken tenderloin ball tip beef ribs pork loin.",
                        phone = null,
                        countryCode = null,
                        email = null,
                        address = null,
                        url = null,
                        imageUrls = emptyList(),
                        type = AdType.HOME,
                        isResolved = false,
                        expirationDate = null,
                        visible = true
                    )
                ),
                isLoading = false,
                error = null,
            ),
        )
}
