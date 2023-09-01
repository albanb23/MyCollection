package com.albab.mycollection.view.collection.favorites

import com.albab.mycollection.domain.model.Collection

sealed interface FavoritesUIState {
    object Loading : FavoritesUIState
    data class Error(val throwable: Throwable) : FavoritesUIState
    data class Success(val collections: List<Collection>) : FavoritesUIState
}