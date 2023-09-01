package com.albab.mycollection.view.common

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.albab.mycollection.view.navigation.destinations

@Composable
fun MyBottomAppBar(navController: NavController) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentScreen = destinations.find { it.route == currentDestination?.route }
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.secondary
    ) {
        destinations.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.icon!!,
                        contentDescription = "Icon",
                        tint = if (currentScreen?.route == screen.route) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = screen.title!!),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                },
                selected = currentScreen?.route == screen.route,
                alwaysShowLabel = false,
                onClick = {
                    if (currentScreen?.route != screen.route) {
                        navController.navigate(screen.route)
                    }
                },
//                selectedContentColor = MaterialTheme.colorScheme.secondary,
//                unselectedContentColor = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}