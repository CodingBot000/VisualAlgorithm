package com.codingbot.algorithm.ui.sorting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.data.SortingData
import com.codingbot.algorithm.ui.theme.Color
import com.codingbot.algorithm.ui.theme.Dimens

enum class SortingBarCellType {
    SortingArray, SortingResult
}
@Composable
fun SortingBarCell(
    sortingBarCellType: SortingBarCellType = SortingBarCellType.SortingArray,
    item: SortingData,
    screenWidth: Dp,
    listSize: Int) {
    val logger = remember { Logger("SortingBarCell", true, "[Screen]") }

    val elementWidth = (screenWidth.value / listSize).dp
    val elementInnerWidth = elementWidth - 2.dp
    Column(
        modifier = Modifier
            .width(elementWidth),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(elementInnerWidth)
                .height(Dimens.Sorting.SortingGraphHeight)
                .background(color = Color.Blue_Gray_10),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(item.scaledNum.dp)
                    .background(
                        color =
                            if (sortingBarCellType == SortingBarCellType.SortingResult) {
                                Color.Red_50
                            } else {
                                if (item.swap1) {
                                    Color.Blue_30
                                } else if (item.swap2) {
                                    Color.Blue_Gray_30
                                } else {
                                    Color.Blue_90
                                }
                           },
                    )
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(elementInnerWidth)
                .padding(5.dp)
                .background(
                    color =
                        if (sortingBarCellType == SortingBarCellType.SortingResult) {
                            Color.White
                        } else {
                            if (item.swap1) {
                                Color.Blue_30
                            } else if (item.swap2) {
                                Color.Blue_Gray_30
                            } else {
                                Color.White
                            }
                        },
                    shape = RoundedCornerShape(Dimens.Sorting.SortingTextOverayMarkRoundedCorner),
                ),
        ) {
            logger { "item.element.toString():${item.element.toString()}" }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(elementInnerWidth)
                    .background(
                        color =
                        if (sortingBarCellType == SortingBarCellType.SortingResult) {
                            Color.White
                        } else {
                            if (item.swap1) {
                                Color.Blue_30
                            } else if (item.swap2) {
                                Color.Blue_Gray_30
                            } else {
                                Color.White
                            }
                        },
                        shape = RoundedCornerShape(Dimens.Sorting.SortingTextOverayMarkRoundedCorner),
                    ),
            ) {
                Text(
                    text = item.element.toString(),
                    color = Color.Black,
                    fontSize = 10.sp,
                    fontWeight = if (item.swap1) {
                        FontWeight.Bold
                    } else if (item.swap2) {
                        FontWeight.ExtraBold
                    } else {
                        FontWeight.Normal
                    }
                )
            }
        }
    }
}