package com.algorithm.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.algorithm.presentation.R
import com.algorithm.presentation.ui.theme.CustomTheme

@Composable
fun ScreenTitle(
    title: String,
    onClickBack: () -> Unit,
    trailingIcon: @Composable () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_arrow_left),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .padding(end = 10.dp)
                .clickableSingle {
                    onClickBack()
                },
            tint = CustomTheme.colors.textColorPrimary
        )

        Text(
            text = title.replace("_", " "),
            color = CustomTheme.colors.textColorPrimary,
            style = CustomTheme.typography.title3Bold
        )

        Spacer(modifier = Modifier.weight(1f))
        trailingIcon()
    }
}

@Composable
fun TopIcon(
    imageVector: ImageVector = Icons.Filled.Info,
    onClick: () -> Unit
) {
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        modifier = Modifier
            .width(32.dp)
            .aspectRatio(1f)
            .clickableSingle {
                onClick()
            },
        tint = CustomTheme.colors.textColorPrimary
    )
}