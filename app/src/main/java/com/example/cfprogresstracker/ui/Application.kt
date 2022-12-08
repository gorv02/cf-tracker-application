package com.example.cfprogresstracker.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.cfprogresstracker.ui.components.BottomNavigationBar
import com.example.cfprogresstracker.ui.components.Toolbar
import com.example.cfprogresstracker.ui.controllers.SearchWidgetState
import com.example.cfprogresstracker.ui.controllers.ToolbarController
import com.example.cfprogresstracker.ui.controllers.ToolbarStyles
import com.example.cfprogresstracker.ui.navigation.NavigationHost
import com.example.cfprogresstracker.ui.navigation.Screens
import com.example.cfprogresstracker.ui.theme.CodeforcesProgressTrackerTheme
import com.example.cfprogresstracker.viewmodel.MainViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Application(
    mainViewModel: MainViewModel,
    navigateToLoginActivity: () -> Unit
) {
    val navController = rememberNavController()

    val topAppBarDefault = TopAppBarDefaults.pinnedScrollBehavior()

    val toolbarController = remember {
        object : ToolbarController {
            override var title: String by mutableStateOf(Screens.ContestsScreen.title)

            override var scrollBehavior: TopAppBarScrollBehavior  by mutableStateOf(
                topAppBarDefault
            )

            override var toolbarStyle: ToolbarStyles by mutableStateOf(ToolbarStyles.Small)

            override var onClickNavUp: () -> Unit by mutableStateOf({})

            override var searchWidgetState: SearchWidgetState by mutableStateOf(
                SearchWidgetState.Closed()
            )

            override var actions: @Composable RowScope.() -> Unit by mutableStateOf({})

            override fun clearActions() {
                actions = {}
            }
        }
    }

    val topBar: @Composable () -> Unit = {
        Toolbar(
            toolbarController = toolbarController
        )
    }

    val bottomBarHeight = 75.dp
    val bottomBarHeightPx = with(LocalDensity.current) { bottomBarHeight.roundToPx().toFloat() }
    var bottomBarOffsetHeightPx by remember { mutableStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val newOffset = bottomBarOffsetHeightPx + available.y
                bottomBarOffsetHeightPx = newOffset.coerceIn(-bottomBarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    val bottomNavBar: @Composable () -> Unit = {
        BottomNavigationBar(
            navController = navController,
            currentScreen = toolbarController.title,
            bottomBarHeight = bottomBarHeight,
            bottomBarOffsetHeightPx = bottomBarOffsetHeightPx
        )
    }

    CodeforcesProgressTrackerTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            Scaffold(
                topBar = topBar,
                bottomBar = bottomNavBar,
            ) {
                NavigationHost(
                    toolbarController = toolbarController,
                    navController = navController,
                    mainViewModel = mainViewModel,
                    paddingValues = it,
                    navigateToLoginActivity = navigateToLoginActivity
                )
            }
        }
    }
}
