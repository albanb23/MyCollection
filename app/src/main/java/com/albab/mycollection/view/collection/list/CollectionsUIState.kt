package com.albab.mycollection.view.collection.list

import com.albab.mycollection.domain.model.Collection

sealed interface CollectionsUIState {
    object Loading : CollectionsUIState
    data class Error(val throwable: Throwable) : CollectionsUIState
    data class Success(val collections: List<Collection>) : CollectionsUIState
}