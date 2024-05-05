package com.codingbot.algorithm.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.codingbot.algorithm.R
import com.codingbot.algorithm.ui.theme.CustomTheme
import com.codingbot.algorithm.ui.theme.CustomTypography

@Composable
fun ScreenTitle(
    title: String,
    onClickBack: () -> Unit
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
                .size(24.dp)
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
    }
}