package com.siddiqui.myapplication.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.siddiqui.myapplication.model.Screen
import com.siddiqui.myapplication.view.RecordAudio
import com.siddiqui.myapplication.viewModel.RecordingListTab
import com.siddiqui.myapplication.viewModel.RecordingViewModel


@Composable
fun NavigationStack(onExitTabs :()-> Unit){
    val navController = rememberNavController()
    val context = LocalContext.current
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf(Screen.Record, Screen.Player, Screen.Setting)
    val viewModel = remember { RecordingViewModel(context = context) }

    // Connect navigation controller with tab selection
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            val route = backStackEntry.destination.route
            selectedTabIndex = tabs.indexOfFirst { it.route == route }.coerceAtLeast(0)
        }
    }
    Scaffold(
        topBar = {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                contentColor = Color.Black,
                backgroundColor = Color.Transparent,
            ) {
                tabs.forEachIndexed {
                        index, screen ->
                    Tab(
                        text = { Text(screen. title) },
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index
                        navController.navigate(screen.route){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true

                            }
                            launchSingleTop = true
                            restoreState = true

                        }


                        }
                    )
                }

            }

            when(selectedTabIndex){
                0-> Screen.Record.route
                1-> Screen.Player.route
                2-> Screen.Setting.route
            }

        }
    ) {paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Record.route,
            modifier = Modifier.padding(paddingValues)
        ){
            composable(Screen.Record.route) {
                RecordAudio(viewModel = viewModel)
            }
            composable(Screen.Player.route) {
                RecordingListTab(viewModel)
            }
            composable(Screen.Setting.route) {
                Text(text = "Setting Screen")
            }

        }
    }
BackHandler {
    if (navController.previousBackStackEntry != null){
        navController.navigateUp()

    }else {
        onExitTabs()
    }
}

}