package com.albab.mycollection.view.photocard.details

import com.albab.mycollection.domain.model.Photocard

sealed interface PhotocardDetailsUIState {
    object Loading : PhotocardDetailsUIState
    data class Error(val throwable: Throwable) : PhotocardDetailsUIState
    data class Success(val photocard: Photocard) : PhotocardDetailsUIState
}