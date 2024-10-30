package com.algorithm.presentation.ui.screens.sorting

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.algorithm.presentation.ui.theme.CustomTheme
import com.algorithm.presentation.ui.theme.Dimens

enum class SortingBarCellType {
    SortingArray, SortingResult
}
@Composable
fun SortingBarCell(
    sortingBarCellType: SortingBarCellType = SortingBarCellType.SortingArray,
    item: com.algorithm.model.SortingData,
    elementWidth: Dp
) {
    val logger = remember { com.algorithm.utils.Logger("SortingBarCell", true, "[Screen]") }
    val elementInnerWidth = elementWidth - 2.dp

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
                .background(color = CustomTheme.colors.elementBarBackground),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(item.scaledNum.dp)
                    .background(
                        color =
                            if (sortingBarCellType == SortingBarCellType.SortingResult) {
                                CustomTheme.colors.elementBarResult
                            } else {
                                if (item.swap1) {
                                    CustomTheme.colors.elementSwap1
                                } else if (item.swap2) {
                                    CustomTheme.colors.elementSwap2
                                } else {
                                    CustomTheme.colors.elementNormal
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
                                CustomTheme.colors.elementSwap1
                            } else if (item.swap2) {
                                CustomTheme.colors.elementSwap2
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
                    style = if (sortingBarCellType == SortingBarCellType.SortingResult) {
                                CustomTheme.typography.caption2Bold
                            } else {
                                if (item.swap1) {
                                    CustomTheme.typography.caption2Bold
                                } else if (item.swap2) {
                                    CustomTheme.typography.caption2ExtraBold
                                } else {
                                    CustomTheme.typography.caption2Regular
                                }
                            }
                )
            }
        }
    }
}