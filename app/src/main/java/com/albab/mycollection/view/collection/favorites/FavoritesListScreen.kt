package com.albab.mycollection.view.collection.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.albab.mycollection.view.collection.CollectionViewModel
import com.albab.mycollection.view.common.Loading

@Composable

fun FavoritesListScreen(
    collectionViewModel: CollectionViewModel,
    onCollectionClick: (String) -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<FavoritesUIState>(
        initialValue = FavoritesUIState.Loading,
        key1 = lifecycle,
        key2 = collectionViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            collectionViewModel.favoritesUIState?.collect { value = it }
        }
    }

    when (uiState) {
        is FavoritesUIState.Success -> {
            val favoritesList = (uiState as FavoritesUIState.Success).collections
            FavoritesList(favorites = favoritesList, onCollectionClick = onCollectionClick)
        }

        is FavoritesUIState.Error -> {}
        is FavoritesUIState.Loading -> {
            Loading()
        }
    }
}