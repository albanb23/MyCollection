package com.albab.mycollection.view.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.albab.mycollection.view.HomeScreen
import com.albab.mycollection.view.collection.CollectionViewModel
import com.albab.mycollection.view.collection.details.CollectionDetailsScreen
import com.albab.mycollection.view.photocard.PhotocardViewModel
import com.albab.mycollection.view.photocard.details.PhotocardDetailsScreen
import com.albab.mycollection.view.photocard.template.PhotocardTemplateScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyCollectionNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit
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
                onCollectionClick = { collectionId ->
                    navHostController.navigateToCollection(
                        collectionId
                    )
                }
            )

        }
        composable(
            route = Collection.routeWithArgs,
            arguments = Collection.arguments
        ) { navBackStackEntry ->
            val collectionId = navBackStackEntry.arguments?.getString(Collection.collectionIdArg)
            collectionId?.let { colId ->
                collectionViewModel.getCollectionById(colId)
                photocardViewModel.getPhotocardByCollection(colId)
                CollectionDetailsScreen(
                    collectionViewModel,
                    photocardViewModel,
                    navigateToTemplate = { collectionId ->
                        navHostController.navigateToPCTemplate(collectionId)
                    },
                    onBackPressed = onBackPressed)
            }
        }
        composable(
            route = Photocard.routeWithArgs,
            arguments = Photocard.arguments
        ) { navBackStackEntry ->
            val pcId = navBackStackEntry.arguments?.getString(Photocard.photocardIdArg)
            pcId?.let { itId ->
                photocardViewModel.getPhotocardById(itId)
                PhotocardDetailsScreen(
                    photocardViewModel = photocardViewModel
                )
            }
        }
        composable(
            route = PhotocardTemplate.routeWithArgs,
            arguments = PhotocardTemplate.arguments
        ) { navBackStackEntry ->
            val collectionId = navBackStackEntry.arguments?.getString(PhotocardTemplate.pcTemplateCollectionIdArg)
            collectionId?.let { itId ->
                PhotocardTemplateScreen(
                    collectionId = itId,
                    photocardViewModel = photocardViewModel,
                    onSuccess = onBackPressed
                )
            }
        }
    }
}

private fun NavHostController.navigateToCollection(collectionId: String) {
    this.navigate("${Collection.route}/$collectionId")
}

private fun NavHostController.navigateToPC(pcId: String) {
    this.navigate("${Photocard.route}/$pcId")
}

private fun NavHostController.navigateToPCTemplate(collectionId: String) {
    this.navigate("${PhotocardTemplate.route}/$collectionId")
}