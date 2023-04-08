package com.gourav.competrace.app_core.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gourav.competrace.app_core.util.Screens
import com.gourav.competrace.app_core.util.TopAppBarManager
import com.gourav.competrace.contests.contests
import com.gourav.competrace.contests.presentation.ContestScreenActions
import com.gourav.competrace.contests.presentation.ContestViewModel
import com.gourav.competrace.problemset.presentation.ProblemSetViewModel
import com.gourav.competrace.problemset.problemSet
import com.gourav.competrace.progress.participated_contests.presentation.ParticipatedContestViewModel
import com.gourav.competrace.progress.progress
import com.gourav.competrace.progress.user.presentation.UserViewModel
import com.gourav.competrace.progress.user_submissions.presentation.UserSubmissionsViewModel
import com.gourav.competrace.settings.SettingsScreen


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CompetraceNavHost(
    sharedViewModel: SharedViewModel,
    appState: CompetraceAppState,
    paddingValues: PaddingValues,
) {
    val contestViewModel: ContestViewModel = hiltViewModel()
    val problemSetViewModel: ProblemSetViewModel = hiltViewModel()
    val userSubmissionsViewModel: UserSubmissionsViewModel = hiltViewModel()
    val userViewModel: UserViewModel = hiltViewModel()
    val participatedContestViewModel: ParticipatedContestViewModel = hiltViewModel()

    NavHost(
        navController = appState.navController,
        startDestination = Screens.ContestsScreen.route,
        modifier = Modifier.padding(
            top = paddingValues.calculateTopPadding()
        )
    ) {

        contests(
            sharedViewModel = sharedViewModel,
            contestViewModel = contestViewModel,
            appState = appState
        )

        problemSet(
            sharedViewModel = sharedViewModel,
            problemSetViewModel = problemSetViewModel,
            appState = appState
        )

        progress(
            sharedViewModel = sharedViewModel,
            userViewModel = userViewModel,
            userSubmissionsViewModel = userSubmissionsViewModel,
            participatedContestViewModel = participatedContestViewModel,
            appState = appState,
            paddingValues = paddingValues
        )

        composable(route = Screens.SettingsScreen.route) {
            LaunchedEffect(Unit) {
                TopAppBarManager.updateTopAppBar(screen = Screens.SettingsScreen)
            }
            SettingsScreen(
                contestViewModel = contestViewModel,
                settingsViewModel = hiltViewModel()
            )
        }
    }
}