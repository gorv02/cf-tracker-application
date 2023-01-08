package com.theruralguys.competrace.ui.navigation.navsections

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.theruralguys.competrace.model.Problem
import com.theruralguys.competrace.retrofit.util.ApiState
import com.theruralguys.competrace.ui.components.CircularIndeterminateProgressBar
import com.theruralguys.competrace.ui.components.ProblemSetScreenActions
import com.theruralguys.competrace.ui.components.RatingRangeSlider
import com.theruralguys.competrace.ui.controllers.TopAppBarController
import com.theruralguys.competrace.ui.navigation.Screens
import com.theruralguys.competrace.ui.screens.NetworkFailScreen
import com.theruralguys.competrace.ui.screens.ProblemSetScreen
import com.theruralguys.competrace.viewmodel.MainViewModel

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.problemSet(
    topAppBarController: TopAppBarController,
    mainViewModel: MainViewModel,

    ) {

    composable(route = Screens.ProblemSetScreen.name) {
        topAppBarController.title = Screens.ProblemSetScreen.title

        val onClickFilterIcon: () -> Unit = {
            topAppBarController.expandToolbar = !topAppBarController.expandToolbar
        }

        var startRatingValue by rememberSaveable {
            mutableStateOf(800)
        }
        var endRatingValue by rememberSaveable {
            mutableStateOf(3500)
        }

        topAppBarController.expandedContent = {
            RatingRangeSlider(
                start = startRatingValue, end = endRatingValue,
                updateStartAndEnd = { start, end ->
                    startRatingValue = start
                    endRatingValue = end
                }
            )
        }

        topAppBarController.actions = {
            ProblemSetScreenActions(
                onClickFilterIcon = onClickFilterIcon,
                isToolbarExpanded = topAppBarController.expandToolbar,
                ratingRange = startRatingValue..endRatingValue
            )
        }



        when (val apiResult = mainViewModel.responseForProblemSet) {
            is ApiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularIndeterminateProgressBar(isDisplayed = true)
                }
            }
            is ApiState.SuccessPS -> {
                if (apiResult.response.status == "OK") {
                    val allProblems: ArrayList<Problem> =
                        apiResult.response.result!!.problems as ArrayList<Problem>

                    val setOfTags = mutableSetOf<String>()
                    allProblems.forEach { problem ->
                        problem.tags?.forEach {
                            setOfTags.add(it)
                        }
                    }
                    mainViewModel.tagList.clear()
                    mainViewModel.tagList.addAll(setOfTags)

                    val filteredProblemList = arrayListOf<Problem>()
                    allProblems.forEach {
                        it.rating?.let { rating ->
                            if (rating in startRatingValue..endRatingValue) filteredProblemList.add(it)
                        }
                    }

                    ProblemSetScreen(
                        listOfProblem = filteredProblemList,
                        contestListById = mainViewModel.contestListById,
                        tagList = mainViewModel.tagList
                    )
                } else {
                    mainViewModel.responseForProblemSet = ApiState.Failure(Throwable())
                }
            }
            is ApiState.Failure -> {
                NetworkFailScreen(onClickRetry = { mainViewModel.getProblemSet() })
            }
            is ApiState.Empty -> {}
            else -> {
                // Nothing
            }
        }
    }
}