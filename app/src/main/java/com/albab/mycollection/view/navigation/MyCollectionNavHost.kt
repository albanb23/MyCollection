package com.albab.mycollection.view.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.albab.mycollection.view.HomeScreen
import com.albab.mycollection.view.collection.CollectionViewModel
import com.albab.mycollection.view.collection.details.CollectionScreen
import com.albab.mycollection.view.collection.favorites.FavoritesListScreen
import com.albab.mycollection.view.photocard.PhotocardViewModel
import com.albab.mycollection.view.photocard.template.PhotocardTemplateScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyCollectionNavHost(
    navHostController: NavHostController,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val collectionViewModel: CollectionViewModel = hiltViewModel()
    val photocardViewModel: PhotocardViewModel = hiltViewModel()

    NavHost(
        navController = navHostController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        composable(route = Home.route) {
            HomeScreen(
                collectionViewModel = collectionViewModel,
                onCollectionClick = { navHostController.navigateToCollection(it) }
            )
        }
        composable(
            route = Collection.routeWithArgs,
            arguments = Collection.arguments
        ) { navBackStackEntry ->
            val collectionId = navBackStackEntry.arguments?.getString(Collection.collectionIdArg)
            collectionId?.let { colId ->
                collectionViewModel.getCollectionById(collectionId)
                CollectionScreen(
                    collectionId = colId,
                    collectionViewModel = collectionViewModel,
                    photocardViewModel = photocardViewModel,
                    navigateToCollection = { navHostController.navigateToCollection(it) },
                    navigateToPCTemplate = { navHostController.navigateToPCTemplate(it) },
                    onBackPressed = onBackPressed
                )
            }
        }
        composable(
            route = PhotocardTemplate.routeWithArgs,
            arguments = PhotocardTemplate.arguments
        ) { navBackStackEntry ->
            val collectionId =
                navBackStackEntry.arguments?.getString(PhotocardTemplate.pcTemplateCollectionIdArg)
            collectionId?.let { itId ->
                PhotocardTemplateScreen(
                    collectionId = itId,
                    photocardViewModel = photocardViewModel,
                    onSuccess = onBackPressed
                )
            }
        }
        composable(route = Favorites.route) {
            collectionViewModel.getFavoriteCollections()
            FavoritesListScreen(
                collectionViewModel = collectionViewModel,
                onCollectionClick = { navHostController.navigateToCollection(it) }
            )
        }
        composable(route = Settings.route) {

        }
    }
}

private fun NavHostController.navigateToCollection(collectionId: String) {
    this.navigate("${Collection.route}/$collectionId")
}

private fun NavHostController.navigateToPCTemplate(collectionId: String) {
    this.navigate("${PhotocardTemplate.route}/$collectionId")
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }