package com.albab.mycollection.view.photocard.list

import com.albab.mycollection.domain.model.CollectionPhotocard

sealed interface PhotocardListUIState {
    object Loading : PhotocardListUIState
    data class Error(val throwable: Throwable) : PhotocardListUIState
    data class Success(val photocards: List<CollectionPhotocard>) : PhotocardListUIState
}