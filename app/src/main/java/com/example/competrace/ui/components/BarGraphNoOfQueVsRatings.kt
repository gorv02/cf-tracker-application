package com.example.competrace.ui.components

import android.graphics.Point
import android.graphics.Typeface
import androidx.compose.animation.core.FloatTweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.competrace.ui.theme.*
import android.graphics.Paint as Paint1


@Composable
fun BarGraphNoOfQueVsRatings(heights: Array<Int>, questionCount: Array<Int>, stepSizeOfGraph: Int) {

    val width = 69
    val gap = 22
    val top = 150f
    val bottom = 750f
    val start = 160f
    val end = start + 750f

    val ratingArray = arrayListOf(
        "800 - 1199",
        "1200 - 1399",
        "1400 - 1599",
        "1600 - 1899",
        "1900 - 2099",
        "2100 - 2399",
        "2400 - 4000",
        "Incorrect Submissions"
    )

    val colors = arrayListOf(
        NewbieGray,
        PupilGreen,
        SpecialistCyan,
        ExpertBlue,
        CandidMasterViolet,
        MasterOrange,
        GrandmasterRed,
        MaterialTheme.colorScheme.onSurface
    )

    val points = arrayListOf<Point>()
    for (i in 0..7) {
        points.add(Point((i + 1) * gap + i * width, heights[i]))
    }

    val context = LocalContext.current
    val textColor = MaterialTheme.colorScheme.onSurface.toArgb()

    var begin by remember { mutableStateOf(false) }

    val animateHeight by animateFloatAsState(
        targetValue = if (begin) 1f else 0f,
        animationSpec = FloatTweenSpec(duration = 1000)
    )

    val screenWidthDp: Int = context.resources.configuration.screenWidthDp + 32

    val primary = MaterialTheme.colorScheme.primary
    val onSurface = MaterialTheme.colorScheme.onSurface

    var status by remember {
        mutableStateOf("Total Question Attempted: " + questionCount.sum())
    }

    val onClickedCanvas: (Offset) -> Unit = {
        var index = -1
        for ((i, point) in points.withIndex()) {
            if (it.x > point.x + start && it.x < point.x + start + width && it.y > bottom - point.y && it.y < bottom) {
                index = i
                break
            }
        }
        status = when (index) {
            -1 -> "Total Question Attempted: " + questionCount.sum()
            7 -> "Incorrect/Partial Submissions: ${questionCount[index]}"
            else -> "For Rating (${ratingArray[index]}): ${questionCount[index]}"
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenWidthDp.dp)
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = onClickedCanvas
                )
            }
    ) {
        drawLine(
            start = Offset(x = start, y = bottom),
            end = Offset(x = start, y = top),
            color = onSurface,
            strokeWidth = 1f
        )
        drawLine(
            start = Offset(x = start, y = bottom),
            end = Offset(x = end, y = bottom),
            color = onSurface,
            strokeWidth = 1f
        )

        for (i in 1..11) {
            drawLine(
                start = Offset(x = start, y = bottom - 50f * i),
                end = Offset(x = end, y = bottom - 50f * i),
                color = onSurface,
                strokeWidth = 0.4f
            )
            drawIntoCanvas {
                it.nativeCanvas.drawText(
                    /* text = */ "${stepSizeOfGraph * i}",
                    /* x = */ 140f,            // x-coordinates of the origin (top left)
                    /* y = */ bottom - 50f * i + 7f, // y-coordinates of the origin (top left)
                    /* paint = */ getTextPaint(textColor, 20f, Paint1.Align.RIGHT)
                )
            }
        }

        /* drawRoundRect(
             color = primary,
             topLeft = Offset(
                 x = 700f,
                 y = 135f
             ),
             size = Size(width = 260f, height = 260f),
             alpha = 0.5f,
             cornerRadius = CornerRadius(x = 5f, y = 5f)
         )*/



        drawIntoCanvas {
            it.nativeCanvas.drawText(
                /* text = */ "Rating",
                /* x = */ 540f,            // x-coordinates of the origin (top left)
                /* y = */ 955f, // y-coordinates of the origin (top left)
                /* paint = */ getTextPaint(textColor, 48f, Paint1.Align.CENTER,)
            )
        }

        drawIntoCanvas {
            it.nativeCanvas.drawText(
                /* text = */ status,
                /* x = */ 500f,            // x-coordinates of the origin (top left)
                /* y = */ 80f, // y-coordinates of the origin (top left)
                /* paint = */ getTextPaint(textColor, 48f, Paint1.Align.CENTER)
            )
        }

        begin = true

        for (i in 0..7) {
            drawRect(
                color = colors[i],
                topLeft = Offset(
                    x = points[i].x + start,
                    y = bottom - (points[i].y) * animateHeight
                ),
                size = Size(width = width.toFloat(), height = (points[i].y) * animateHeight)
            )
        }
    }
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenWidthDp.dp)
            .padding(16.dp)
            .rotate(-90f)
    ) {
        for (i in 0..7) {
            drawIntoCanvas {
                it.nativeCanvas.drawText(
                    /* text = */ ratingArray[i],
                    /* x = */ 270f,            // x-coordinates of the origin (top left)
                    /* y = */ 270f + i * (width + gap), // y-coordinates of the origin (top left)
                    /* paint = */ getTextPaint(textColor,20f, Paint1.Align.RIGHT)
                )
            }
        }
        drawIntoCanvas {
            it.nativeCanvas.drawText(
                /* text = */ "No. of Questions",
                /* x = */ 560f,            // x-coordinates of the origin (top left)
                /* y = */ 120f, // y-coordinates of the origin (top left)
                /* paint = */ getTextPaint(textColor,48f, Paint1.Align.CENTER)
            )
        }
    }
}

fun getTextPaint(
    textColor: Int, size: Float,
    alignment: Paint1.Align,
) = Paint1().apply {
        textSize = size
        color = textColor
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        textAlign = alignment
    }
