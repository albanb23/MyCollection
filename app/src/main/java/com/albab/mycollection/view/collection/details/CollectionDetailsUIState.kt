package com.albab.mycollection.view.collection.details

import com.albab.mycollection.domain.model.Collection

sealed interface CollectionDetailsUIState {
    object Loading : CollectionDetailsUIState
    data class Error(val throwable: Throwable) : CollectionDetailsUIState
    data class Success(val collection: Collection) : CollectionDetailsUIState
}