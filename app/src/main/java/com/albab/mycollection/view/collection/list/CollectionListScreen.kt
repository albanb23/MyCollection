package com.albab.mycollection.view.collection.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.albab.mycollection.domain.model.Collection
import com.albab.mycollection.view.collection.CollectionViewModel
import com.albab.mycollection.view.common.Loading
import com.albab.mycollection.view.common.SearchModelState
import com.albab.mycollection.view.common.SearchScreen

@Composable
fun CollectionListScreen(
    collectionViewModel: CollectionViewModel,
    onCollectionClick: (String) -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<CollectionsUIState>(
        initialValue = CollectionsUIState.Loading,
        key1 = lifecycle,
        key2 = collectionViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            collectionViewModel.collectionListUiState.collect { value = it }
        }
    }

    when (uiState) {
        is CollectionsUIState.Success -> {
            val collections = (uiState as CollectionsUIState.Success).collections
            val searchModelState by collectionViewModel.searchModelState.collectAsState(
                initial = SearchModelState(
                    "",
                    collections
                )
            )
            val list = searchModelState.list.ifEmpty { collections }
            SearchScreen(
                searchText = searchModelState.searchText,
                matchesFound = searchModelState.resultsFound,
                onTextChanged = { collectionViewModel.onSearchTextChanged(it) },
                onTextCleared = { collectionViewModel.onSearchTextChanged("") }) {
                CollectionList(
                    viewModel = collectionViewModel,
                    collections = list as List<Collection>,
                    onCollectionClick = onCollectionClick
                )
            }
        }

        is CollectionsUIState.Error -> {}
        CollectionsUIState.Loading -> {
            Loading()
        }
    }
}