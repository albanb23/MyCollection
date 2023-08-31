package com.albab.mycollection.view.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.albab.mycollection.R

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchText: String,
    matchesFound: Boolean,
    onTextChanged: (String) -> Unit,
    onTextCleared: () -> Unit,
    results: @Composable () -> Unit = {}
) {
    MySearchBar(
        searchText = searchText,
        onTextChanged = onTextChanged,
        onTextCleared = onTextCleared,
        modifier = modifier.padding(16.dp)
    )
    if (matchesFound) {
        results()
    } else {
        NoSearchResults()
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MySearchBar(
    modifier: Modifier = Modifier,
    searchText: String,
    onTextChanged: (String) -> Unit,
    onTextCleared: () -> Unit
) {
    var showClearButton by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = searchText,
        onValueChange = onTextChanged,
        trailingIcon = {
            AnimatedVisibility(visible = showClearButton) {
                IconButton(onClick = onTextCleared) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                }
            }
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        },
        placeholder = {
            Text(text = stringResource(id = R.string.search))
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
        }),
        shape = RoundedCornerShape(30.dp),
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                showClearButton = (focusState.isFocused)
            }
    )
}

@Composable
fun NoSearchResults(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.no_search_results_found))
    }
}