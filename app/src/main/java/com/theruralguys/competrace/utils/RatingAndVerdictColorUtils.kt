package com.theruralguys.competrace.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.theruralguys.competrace.ui.theme.*

@Composable
fun getRatingTextColor(rating: Int?) = when(rating){
    in 800..1199 -> NewbieGray
    in 1200..1399 -> PupilGreen
    in 1400..1599 -> SpecialistCyan
    in 1600..1899 -> ExpertBlue
    in 1900..2099 -> CandidMasterViolet
    in  2100..2399 -> MasterOrange
    in 2400..4000 -> GrandmasterRed
    else -> MaterialTheme.colorScheme.onSurface
}

@Composable
fun getRatingContainerColor(rating: Int?) = when(rating){
    in 800..1199 -> light_NewbieGrayContainer
    in 1200..1399 -> light_PupilGreenContainer
    in 1400..1599 -> light_SpecialistCyanContainer
    in 1600..1899 -> light_ExpertBlueContainer
    in 1900..2099 -> light_CandidMasterVioletContainer
    in  2100..2399 -> light_MasterOrangeContainer
    in 2400..4000 -> light_GrandmasterRedContainer
    else -> MaterialTheme.colorScheme.primaryContainer
}

fun getVerdictColor(lastVerdict: String?, hasVerdictOK: Boolean) = if (hasVerdictOK) CorrectGreen else {
    lastVerdict.let {
        if (it == Verdict.TESTING) TestingGreen
        else if (Verdict.RED.contains(it)) IncorrectRed
        else PartialYellow
    }
}