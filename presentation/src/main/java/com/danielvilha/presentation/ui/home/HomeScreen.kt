package com.danielvilha.presentation.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.danielvilha.model.Ad
import com.danielvilha.presentation.util.ExcludeFromJacocoGeneratedReport
import com.danielvilha.presentation.util.FindlyTopBar
import com.danielvilha.presentation.util.LightDarkPreview
import com.danielvilha.theme.FindlyTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlin.collections.listOf

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun HomeScreenPreview(
    @PreviewParameter(HomeProvider::class)
    value: HomeState
) {
    FindlyTheme {
        HomeScreen(
            state = value
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(state: HomeState) {
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var showLogoutDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = FirebaseAuth.getInstance().currentUser?.email ?: "Unknown",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = { Text(text = "My Ads") },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                state.onNavigateToMyAds()
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    NavigationDrawerItem(
                        label = { Text(text = "Logout") },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                showLogoutDialog = true
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                FindlyTopBar(
                    title = "Findly",
                    showBackButton = false,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { state.onCreateAd() },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape,
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Ad")
                }
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {
                    if (state.ads.isEmpty() == true) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "No ads available.")
                        }
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(state.ads) { ad ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { state.onAdClick(ad.id.toString()) },
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = ad.title, style = MaterialTheme.typography.titleMedium)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = ad.description,
                                            style = MaterialTheme.typography.bodyMedium,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = "Type: ${ad.type?.name}", style = MaterialTheme.typography.bodySmall)
                                        if (ad.visible ) {
                                            Text(
                                                text = "✅",
                                                color = MaterialTheme.colorScheme.primary,
                                                style = MaterialTheme.typography.labelSmall
                                            )
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

                if (showLogoutDialog) {
                    AlertDialog(
                        onDismissRequest = { showLogoutDialog = false },
                        confirmButton = {
                            TextButton(onClick = {
                                FirebaseAuth.getInstance().signOut()
                                showLogoutDialog = false
                                state.onLogoutConfirmed()
                            }) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showLogoutDialog = false }) {
                                Text("No")
                            }
                        },
                        title = { Text("Do you want to logoff?") },
                        text = { Text("You will be logged out of your account.") }
                    )
                }
            }
        )
    }
}

@ExcludeFromJacocoGeneratedReport
private class HomeProvider : PreviewParameterProvider<HomeState> {
    override val values: Sequence<HomeState>
        get() = sequenceOf(
            HomeState(),
            HomeState(
                ads = emptyList<Ad>(),
                isLoading = false,
            ),
            HomeState(
                ads = emptyList<Ad>(),
                isLoading = true,
            ),
            HomeState(
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
                        type = null,
                        isResolved = false,
                        expirationDate = null,
                        visible = true
                    )
                ),
                isLoading = false,
            ),
        )
}