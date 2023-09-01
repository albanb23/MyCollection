package com.albab.mycollection.view.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.albab.mycollection.R

interface MyCollectionDestinations {
    val title: Int?
    val icon: ImageVector?
    val route: String
    val showBottomBar: Boolean
}

object Home : MyCollectionDestinations {
    override val title: Int
        get() = R.string.my_collections
    override val icon: ImageVector
        get() = Icons.Default.Home
    override val route: String
        get() = "home"
    override val showBottomBar: Boolean
        get() = true
}

object Collection: MyCollectionDestinations {
    override val title: Int?
        get() = null
    override val icon: ImageVector?
        get() = null
    override val route: String
        get() = "collection"
    override val showBottomBar: Boolean
        get() = false
    const val collectionIdArg = "id"
    val routeWithArgs = "$route/{$collectionIdArg}"
    val arguments = listOf(
        navArgument(collectionIdArg) { type = NavType.StringType }
    )
}

object PhotocardTemplate: MyCollectionDestinations {
    override val title: Int?
        get() = null
    override val icon: ImageVector?
        get() = null
    override val route: String
        get() = "photocard_template"
    override val showBottomBar: Boolean
        get() = false
    const val pcTemplateCollectionIdArg = "id"
    val routeWithArgs = "$route/{$pcTemplateCollectionIdArg}"
    val arguments = listOf(
        navArgument(pcTemplateCollectionIdArg) { type = NavType.StringType }
    )
}

object Favorites: MyCollectionDestinations{
    override val title: Int
        get() = R.string.favorites
    override val icon: ImageVector
        get() = Icons.Default.Favorite
    override val route: String
        get() = "favorites"
    override val showBottomBar: Boolean
        get() = true
}

object Settings: MyCollectionDestinations{
    override val title: Int
        get() = R.string.settings
    override val icon: ImageVector
        get() = Icons.Default.Settings
    override val route: String
        get() = "settings"
    override val showBottomBar: Boolean
        get() = true
}

val destinations = listOf(Home, Favorites, Settings)