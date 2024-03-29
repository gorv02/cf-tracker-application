package com.gourav.competrace.progress.user_submissions.model

import com.gourav.competrace.contests.model.CompetraceContest
import com.gourav.competrace.problemset.model.CodeforcesProblem

data class Submission(
    val author: Author?,
    val contestId: Int?,
    val creationTimeSeconds: Int,
    val id: Int,
    val memoryConsumedBytes: Int,
    val passedTestCount: Int,
    val points: Double?,
    val pointsInfo: String?,
    val problem: CodeforcesProblem,
    val programmingLanguage: String,
    val relativeTimeSeconds: Int,
    val testset: String?,
    val timeConsumedMillis: Int,
    val verdict: String
) {
    fun creationTimeInMillis() = creationTimeSeconds * 1000L
    private val gymOrContest = contestId?.let { if (it > 100000) "gym" else "contest" } ?: "contest"
    fun getLink() = "https://codeforces.com/$gymOrContest/$contestId/submission/$id"

    fun isSubmittedDuringContest(contest: CompetraceContest) =
        creationTimeInMillis() in contest.startTimeInMillis..contest.endTimeInMillis
}
