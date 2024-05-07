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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.data.SortingData
import com.codingbot.algorithm.ui.theme.Color
import com.codingbot.algorithm.ui.theme.CustomTheme
import com.codingbot.algorithm.ui.theme.Dimens

enum class SortingBarCellType {
    SortingArray, SortingResult
}
@Composable
fun SortingBarCell(
    sortingBarCellType: SortingBarCellType = SortingBarCellType.SortingArray,
    item: SortingData,
    elementWidth: Dp
) {
    val logger = remember { Logger("SortingBarCell", true, "[Screen]") }
    val elementInnerWidth = elementWidth - 2.dp
    logger { "elementWidth : $elementWidth $elementInnerWidth"}
    Column(
        modifier = Modifier
            .background(color = CustomTheme.colors.bg)
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
                .size(elementWidth)
                .padding(5.dp),
        ) {
            logger { "item.element.toString():${item.element.toString()}" }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(elementInnerWidth)
                    .background(
                        color =
                        if (sortingBarCellType == SortingBarCellType.SortingResult) {
                            androidx.compose.ui.graphics.Color.Transparent
                        } else {
                            if (item.swap1) {
                                Color.Blue_30
                            } else if (item.swap2) {
                                Color.Blue_Gray_30
                            } else {
                                androidx.compose.ui.graphics.Color.Transparent
                            }
                        },
                        shape = RoundedCornerShape(Dimens.Sorting.SortingTextOverayMarkRoundedCorner),
                    ),
            ) {
                Text(
                    text = item.element.toString(),
                    color = CustomTheme.colors.textColorPrimary,
                    style = CustomTheme.typography.caption2Regular,
                    fontWeight =
                    if (sortingBarCellType == SortingBarCellType.SortingResult) {
                        FontWeight.Bold
                    } else {
                        if (item.swap1) {
                            FontWeight.Bold
                        } else if (item.swap2) {
                            FontWeight.ExtraBold
                        } else {
                            FontWeight.Normal
                        }
                    }
                )
            }
        }
    }
}