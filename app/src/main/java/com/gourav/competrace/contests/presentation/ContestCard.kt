package com.gourav.competrace.contests.presentation

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Card
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gourav.competrace.R
import com.gourav.competrace.app_core.ui.components.CompetraceIconButton
import com.gourav.competrace.app_core.util.getCurrentTimeInMillis
import com.gourav.competrace.app_core.util.getFormattedTime
import com.gourav.competrace.app_core.util.unixToDMET
import com.gourav.competrace.contests.model.CompetraceContest
import com.gourav.competrace.contests.util.MyCountDownTimer
import com.gourav.competrace.ui.theme.RegistrationRed
import com.gourav.competrace.utils.Phase
import com.gourav.competrace.utils.copyTextToClipBoard
import com.gourav.competrace.utils.loadUrl

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ContestCard(
    contest: CompetraceContest,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val haptic = LocalHapticFeedback.current

    val ratedCategoriesRow: @Composable (Modifier) -> Unit = { modifier1 ->
        LazyRow(
            modifier = modifier1,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(contest.ratedCategories.size) {
                ElevatedAssistChip(onClick = { /*TODO*/ }, label = {
                    Text(
                        text = contest.ratedCategories[it].value,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                })
            }

            item {
                contest.registrationOpen?.let {
                    if (it) {
                        ElevatedAssistChip(
                            onClick = {
                                loadUrl(
                                    context = context,
                                    url = contest.registrationUrl
                                )
                            },
                            label = {
                                Text(
                                    text = "Register Now!",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            },
                            colors = AssistChipDefaults.elevatedAssistChipColors(
                                containerColor = RegistrationRed,
                                labelColor = Color.White
                            )
                        )
                    }
                }
            }
        }
    }

    val onClickContestCard: () -> Unit = {
        loadUrl(context = context, url = contest.websiteUrl)
    }

    val onLongClickContestCard: () -> Unit = {
        Log.d("Copy URL", contest.toString())
        copyTextToClipBoard(
            text = contest.websiteUrl,
            toastMessage = "Contest Link Copied",
            context = context,
            clipboardManager = clipboardManager,
            haptic = haptic
        )
    }

    val cardBgColor =
        if (contest.phase == Phase.CODING) MaterialTheme.colorScheme.secondaryContainer
        else MaterialTheme.colorScheme.surfaceVariant
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .animateContentSize()
            .combinedClickable(
                onClick = onClickContestCard,
                onLongClick = onLongClickContestCard,
            ),
        backgroundColor = cardBgColor,
        contentColor = contentColorFor(backgroundColor = cardBgColor),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(
                    text = contest.name,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 4.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                val startTime = unixToDMET(contest.startTimeInMillis)
                val length = getFormattedTime(contest.durationInMillis, format = "%02d:%02d")

                val startTimeAndLength = buildAnnotatedString {
                    append(startTime)
                    append("  |  ")
                    append(length)
                }

                Text(
                    text = startTimeAndLength,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 1f),
                    modifier = Modifier.padding(bottom = 8.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            val totalTimeInMillis =
                if (contest.phase == Phase.BEFORE)
                    contest.startTimeInMillis - getCurrentTimeInMillis()
                else
                    contest.endTimeInMillis - getCurrentTimeInMillis()

            var timeLeftInMillis by remember { mutableStateOf(totalTimeInMillis) }

            var isStarted by remember { mutableStateOf(false) }
            if (!isStarted) {
                MyCountDownTimer(
                    totalTimeInMillis,
                    onTik = { timeLeftInMillis = it }).start()
                isStarted = true
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (contest.within7Days) AssistChip(
                    onClick = { /*TODO*/ },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_timer_24px),
                            contentDescription = "Timer icon",
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    },
                    label = {
                        Text(
                            text = getFormattedTime(timeLeftInMillis),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                ) else ratedCategoriesRow(Modifier)

                if (contest.phase == Phase.BEFORE) CompetraceIconButton(
                    iconId = R.drawable.ic_calendar_add_on_24px,
                    onClick = { contest.addToCalender(context = context) },
                    contentDescription = "Add to calender",
                )
            }
            if (contest.within7Days) ratedCategoriesRow(Modifier.padding(horizontal = 8.dp))
        }
    }
}