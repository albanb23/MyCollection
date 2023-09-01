package com.albab.mycollection.view.collection.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.albab.mycollection.domain.model.Collection
import com.albab.mycollection.view.collection.list.CollectionItem
import com.albab.mycollection.view.common.MyTopApBar
import com.albab.mycollection.view.navigation.Favorites

@Composable
fun FavoritesList(
    favorites: List<Collection>,
    onCollectionClick: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            MyTopApBar(
                title = Favorites.title,
                topIcon = Icons.Default.Menu,
                topAction = {},
                optionAction = {}
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.padding(8.dp)
            ) {
                items(favorites) { favorite ->
                    CollectionItem(
                        collection = favorite,
                        onCollectionClick = onCollectionClick,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}