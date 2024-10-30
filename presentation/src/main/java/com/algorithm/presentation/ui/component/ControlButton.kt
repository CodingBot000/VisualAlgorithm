package com.algorithm.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.algorithm.presentation.ui.theme.CustomTheme

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
            contentColor = CustomTheme.colors.black,
            disabledBackgroundColor = CustomTheme.colors.buttonBackgroundDisabled,
            disabledContentColor = CustomTheme.colors.black
        ), enabled = isEnable,
        shape = RoundedCornerShape(10.dp),
//        border = BorderStroke(width = 2.dp, color = Color.Blue),
        content = {
            Image(imageVector = ImageVector.vectorResource(resId),
                contentDescription = ""
            )
        })
}
