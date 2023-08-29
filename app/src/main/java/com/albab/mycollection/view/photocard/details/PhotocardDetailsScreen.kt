package com.albab.mycollection.view.photocard.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.albab.mycollection.view.photocard.PhotocardViewModel

@Composable
fun PhotocardDetailsScreen(photocardViewModel: PhotocardViewModel) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val pcUIState by produceState<PhotocardDetailsUIState>(
        initialValue = PhotocardDetailsUIState.Loading,
        key1 = lifecycle,
        key2 = photocardViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            photocardViewModel.photocardDetailsUIState?.collect { value = it }
        }
    }

    when (pcUIState) {
        is PhotocardDetailsUIState.Success -> {
            PhotocardDetails(
                photocard = (pcUIState as PhotocardDetailsUIState.Success).photocard
            ) { photocardViewModel.updatePhotocard(it) }
        }

        is PhotocardDetailsUIState.Error -> {}
        PhotocardDetailsUIState.Loading -> {}
    }
}