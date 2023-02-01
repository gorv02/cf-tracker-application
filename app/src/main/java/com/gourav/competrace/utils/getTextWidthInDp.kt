package com.gourav.competrace.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit

@Composable
fun textUnitToDp(testUnit: TextUnit) =
    with(LocalDensity.current) { testUnit.toDp() }

@Composable
fun getTextWidthInDp(testSize: TextUnit, letters: Int = 1) =
    textUnitToDp(testUnit = testSize) * letters

