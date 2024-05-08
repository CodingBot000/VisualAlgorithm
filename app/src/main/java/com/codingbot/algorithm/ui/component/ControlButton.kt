package com.codingbot.algorithm.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.codingbot.algorithm.ui.theme.CustomTheme


@Composable
fun ControlButton(
    resId: Int,
    onClick:() -> Unit = {},
    isEnable: Boolean = false
) {
    Button(
        onClick = {
            onClick()
        }, colors = ButtonDefaults.buttonColors(
            backgroundColor = CustomTheme.colors.buttonBackground,
            contentColor = Color.Black,
            disabledBackgroundColor = CustomTheme.colors.buttonBackgroundDisabled,
            disabledContentColor = Color.Black
        ), enabled = isEnable,
        shape = RoundedCornerShape(10.dp),
//        border = BorderStroke(width = 2.dp, color = Color.Blue),
        content = {
            Image(imageVector = ImageVector.vectorResource(resId),
                contentDescription = ""
            )
        })
}
