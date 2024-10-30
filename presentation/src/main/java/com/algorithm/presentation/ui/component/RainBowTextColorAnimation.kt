package com.algorithm.presentation.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


@Composable
fun RainBowTextColorAnimation(
    text: String,
    fontSize: TextUnit,
    rainbowColors: List<Color>,
    delayTimer: Long = 500L
) {
    val colorIndices = remember { mutableStateListOf<Int>().apply {
        repeat(text.length) { add(it % rainbowColors.size) }
    } }

    LaunchedEffect(Unit) {
        while (true) {
            delay(delayTimer)
            for (i in colorIndices.indices) {
                colorIndices[i] = (colorIndices[i] + 1) % rainbowColors.size // 색상 인덱스를 변경
            }
        }
    }

    Text(
        modifier = Modifier
            .padding(5.dp),
        text = buildAnnotatedString {
            text.forEachIndexed { index, char ->
                withStyle(style = SpanStyle(color = rainbowColors[colorIndices[index]])) {
                    append(char)
                }
            }
        },
        style = TextStyle(
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    )

}

