package com.albab.mycollection.view

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.albab.mycollection.view.common.MyBottomAppBar
import com.albab.mycollection.view.navigation.MyCollectionNavHost
import com.albab.mycollection.view.navigation.destinations
import com.albab.mycollection.view.ui.theme.MyCollectionTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyCollectionTheme {
                val navController = rememberNavController()
                val currentBackStack by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStack?.destination
                val currentScreen = destinations.find { it.route == currentDestination?.route }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Scaffold(
                        bottomBar = {
                            AnimatedVisibility(
                                visible = currentScreen?.showBottomBar == true,
                                enter = slideInVertically(initialOffsetY = {it}),
                                exit = slideOutVertically(targetOffsetY = {it})
                            ) {
                                MyBottomAppBar(navController = navController)
                            }
                        }
                    ) { paddingValues ->
                        MyCollectionNavHost(
                            navHostController = navController,
                            modifier = Modifier.padding(paddingValues),
                            onBackPressed = { onBackPressed() }
                        )
                    }
                }
            }
        }
    }
}