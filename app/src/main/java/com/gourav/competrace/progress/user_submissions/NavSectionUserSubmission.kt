package com.gourav.competrace.progress.user_submissions

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.gourav.competrace.app_core.ui.components.CompetraceSwipeRefreshIndicator
import com.gourav.competrace.app_core.util.ApiState
import com.gourav.competrace.app_core.util.Screens
import com.gourav.competrace.app_core.util.TopAppBarManager
import com.gourav.competrace.progress.user_submissions.presentation.UserSubmissionsScreen
import com.gourav.competrace.progress.user_submissions.presentation.UserSubmissionsScreenActions
import com.gourav.competrace.progress.user_submissions.presentation.UserSubmissionsViewModel
import com.gourav.competrace.ui.components.SearchAppBar
import com.gourav.competrace.app_core.ui.NetworkFailScreen
import com.gourav.competrace.app_core.util.UserSubmissionFilter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.gourav.competrace.R

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.userSubmission(
    userSubmissionsViewModel: UserSubmissionsViewModel,
) {
    composable(route = Screens.UserSubmissionsScreen.route) {
        val scope = rememberCoroutineScope()

        val contestListById by userSubmissionsViewModel.contestListById.collectAsState()
        val currentSelection by userSubmissionsViewModel.currentSelection.collectAsState()
        val searchQuery by userSubmissionsViewModel.searchQuery.collectAsState()
        val isRefreshing by userSubmissionsViewModel.isUserSubmissionRefreshing.collectAsState()
        val showTags by userSubmissionsViewModel.showTags.collectAsState(initial = true)
        val responseForUserSubmissions by userSubmissionsViewModel.responseForUserSubmissions.collectAsState()

        val searchBarFocusRequester = remember { FocusRequester() }

        val openSearchWidget: () -> Unit = {
            TopAppBarManager.openSearchWidget()
            scope.launch {
                delay(100)
                searchBarFocusRequester.requestFocus()
            }
        }

        LaunchedEffect(Unit){
            TopAppBarManager.updateTopAppBar(
                screen = Screens.UserSubmissionsScreen,
                searchWidget = {
                    SearchAppBar(
                        query = searchQuery,
                        onValueChange = userSubmissionsViewModel::updateSearchQuery,
                        onCloseClicked = TopAppBarManager::closeSearchWidget,
                        modifier = Modifier.focusRequester(searchBarFocusRequester),
                        placeHolderText = stringResource(id = R.string.search_problem_contest)
                    )
                },
                actions = {
                    UserSubmissionsScreenActions(
                        currentSelectionForUserSubmissions = currentSelection,
                        onClickSearch = openSearchWidget,
                        onClickAll = {
                            userSubmissionsViewModel.updateCurrentSelection(UserSubmissionFilter.ALL)
                        },
                        onClickCorrect = {
                            userSubmissionsViewModel.updateCurrentSelection(UserSubmissionFilter.CORRECT)
                        },
                        onClickIncorrect = {
                            userSubmissionsViewModel.updateCurrentSelection(UserSubmissionFilter.INCORRECT)
                        },
                        badgeConditionForSearch = searchQuery.isNotBlank()
                    )
                }
            )
        }

        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = userSubmissionsViewModel::refreshUserSubmission,
            indicator = CompetraceSwipeRefreshIndicator
        ) {
            when (responseForUserSubmissions) {
                is ApiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize())
                }
                is ApiState.Failure -> {
                    NetworkFailScreen(
                        onClickRetry = userSubmissionsViewModel::refreshUserSubmission
                    )
                }
                ApiState.Success -> {
                    val filteredProblemsWithSubmission by
                    userSubmissionsViewModel.filteredProblemsWithSubmissions.collectAsState()

                    UserSubmissionsScreen(
                        submittedProblemsWithSubmissions = filteredProblemsWithSubmission,
                        codeforcesContestListById = contestListById,
                        showTags = showTags
                    )
                }
            }
        }
    }
}