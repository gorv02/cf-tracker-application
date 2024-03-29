package com.gourav.competrace.progress.participated_contests

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gourav.competrace.app_core.TrackScreen
import com.gourav.competrace.app_core.util.ApiState
import com.gourav.competrace.app_core.util.Screens
import com.gourav.competrace.app_core.util.TopAppBarManager
import com.gourav.competrace.progress.participated_contests.presentation.ParticipatedContestViewModel
import com.gourav.competrace.progress.participated_contests.presentation.ParticipatedContestsScreen
import com.gourav.competrace.app_core.ui.FailureScreen
import com.gourav.competrace.app_core.ui.components.CompetracePullRefreshIndicator
import com.gourav.competrace.progress.user_submissions.presentation.UserSubmissionsViewModel

@OptIn(ExperimentalMaterialApi::class)
fun NavGraphBuilder.participatedContests(
    participatedContestViewModel: ParticipatedContestViewModel,
) {
    composable(route = Screens.ParticipatedContestsScreen.route) {
        TrackScreen(screen = Screens.ParticipatedContestsScreen)

        val isRefreshing by participatedContestViewModel.isUserRatingChangesRefreshing.collectAsState()
        val responseForUserRatingChanges by participatedContestViewModel.responseForUserRatingChanges.collectAsState()
        val participatedContests by participatedContestViewModel.participatedContests.collectAsState()

        val pullRefreshState = rememberPullRefreshState(
            refreshing = isRefreshing,
            onRefresh = participatedContestViewModel::refreshUserRatingChanges
        )

        LaunchedEffect(Unit){
            TopAppBarManager.updateTopAppBar(screen = Screens.ParticipatedContestsScreen)
        }

        Box(Modifier.pullRefresh(pullRefreshState)) {
            when (val apiState = responseForUserRatingChanges) {
                is ApiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize())
                }
                is ApiState.Failure -> {
                    FailureScreen(
                        onClickRetry = participatedContestViewModel::refreshUserRatingChanges,
                        errorMessage = apiState.message
                    )
                }
                ApiState.Success -> {
                    ParticipatedContestsScreen(participatedCodeforcesContests = participatedContests)
                }
            }
            CompetracePullRefreshIndicator(refreshing = isRefreshing, state = pullRefreshState)
        }
    }
}