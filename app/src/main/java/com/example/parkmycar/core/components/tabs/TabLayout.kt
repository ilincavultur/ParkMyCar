package com.example.parkmycar.core.components.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabLayout(
    navController: NavController
) {
    val pagerState = rememberPagerState(pageCount = 2)
    Scaffold {
        Column(modifier = Modifier.padding(paddingValues = it)){
            Row{
                Tabs(pagerState = pagerState)
            }
            Row(
                modifier = Modifier.fillMaxHeight()
            ) {
                TabsContent(pagerState = pagerState, navController = navController)
            }
        }
    }
}