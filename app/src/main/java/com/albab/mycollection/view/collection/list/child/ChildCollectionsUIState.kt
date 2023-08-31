package com.albab.mycollection.view.collection.list.child

import com.albab.mycollection.domain.model.CollectionParent

interface ChildCollectionsUIState {
    object Loading : ChildCollectionsUIState
    data class Error(val throwable: Throwable) : ChildCollectionsUIState
    data class Success(val collections: List<CollectionParent>) : ChildCollectionsUIState
}