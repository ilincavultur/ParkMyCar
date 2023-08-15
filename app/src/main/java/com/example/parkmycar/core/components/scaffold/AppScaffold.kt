package com.example.parkmycar.core.components

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Map
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.parkmycar.core.components.bottomnavigation.BottomNavigationItem
import com.example.parkmycar.core.navigation.Navigation
import com.example.parkmycar.core.navigation.Screen

@Composable
fun  AppScaffold() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val snackbarHost = rememberScaffoldState()

    val items : List<BottomNavigationItem> = listOf(
        BottomNavigationItem(
            route = Screen.MapScreen.route,
            icon = Icons.Default.Map,
            resourceId = Screen.MapScreen.resourceId
        ),
        BottomNavigationItem(
            route = Screen.TabLayout.route,
            icon = Icons.Default.BookmarkBorder,
            resourceId = Screen.TabLayout.resourceId
        ),
    )

    val screensWithoutBottomBar = listOf<String>(
        Screen.SplashScreen.route
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHost.snackbarHostState)
        },
        scaffoldState = scaffoldState,
        bottomBar = {
            if (navBackStackEntry?.destination?.route !in screensWithoutBottomBar) {
                BottomNavigation {
                    items.forEach { screen ->
                        val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                        val lineLength = animateFloatAsState(
                            targetValue = if(isSelected) 1f else 0f,
                            animationSpec = tween(
                                durationMillis = 300
                            )
                        )

                        BottomNavigationItem(
                            icon = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(10.dp)
                                        .drawBehind {
                                            if(lineLength.value > 0f) {
                                                drawLine(
                                                    color = if (isSelected) Color.White
                                                    else Color.White,
                                                    start = Offset(
                                                        size.width / 2f - lineLength.value * 15.dp.toPx(),
                                                        size.height
                                                    ),
                                                    end = Offset(
                                                        size.width / 2f + lineLength.value * 15.dp.toPx(),
                                                        size.height
                                                    ),
                                                    strokeWidth = 2.dp.toPx(),
                                                    cap = StrokeCap.Round
                                                )
                                            }
                                        }
                                ) {
                                    Icon(screen.icon, contentDescription = null, modifier = Modifier.align(Alignment.Center))
                                }
                                   },
                            selected = isSelected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        Navigation(navController = navController, innerPadding = innerPadding, snackbarHostState = snackbarHost.snackbarHostState)
    }
}