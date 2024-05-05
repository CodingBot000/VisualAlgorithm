//package com.codingbot.algorithm.ui.component
//
//import androidx.annotation.DrawableRes
//import androidx.compose.foundation.background
//import androidx.compose.foundation.isSystemInDarkTheme
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.heightIn
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.Icon
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.Immutable
//import androidx.compose.runtime.key
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.alpha
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import com.codingbot.algorithm.R
//import com.codingbot.algorithm.ui.theme.CustomColors
//
//
//@Composable
//fun PrimaryButton(
//    modifier: Modifier = Modifier,
//    text: String,
//    onClick: () -> Unit,
//    height: ButtonHeight,
//    key: Any? = null,
//    radius: Dp? = null,
//    enabled: Boolean = true,
//    background: Background.Solid? = null,
//    textColor: Color? = null,
//    debounce: Boolean = false,
//    textWeight: Boolean = false,
//    state: ButtonState = ButtonState.Default,
//    leadingIcon: @Composable ((Color) -> Unit)? = null,
//    trailingIcon: @Composable ((Color) -> Unit)? = null,
//    iconPosition: ButtonIconPosition = ButtonIconPosition.None
//) {
//    StatefulButton(
//        key = key,
//        text = text,
//        state = state,
//        radius = radius,
//        height = height,
//        enabled = enabled,
//        onClick = onClick,
//        modifier = modifier,
//        debounce = debounce,
//        background = background,
//        textColor = textColor,
//        textWeight = textWeight,
//        leadingIcon = leadingIcon,
//        type = ButtonType.Primary,
//        trailingIcon = trailingIcon,
//        iconPosition = iconPosition
//    )
//}
//
//@Composable
//fun SecondaryButton(
//    modifier: Modifier = Modifier,
//    text: String,
//    onClick: () -> Unit,
//    height: ButtonHeight,
//    key: Any? = null,
//    radius: Dp? = null,
//    enabled: Boolean = true,
//    background: Background.Solid? = null,
//    textColor: Color? = null,
//    debounce: Boolean = false,
//    textWeight: Boolean = false,
//    state: ButtonState = ButtonState.Default,
//    leadingIcon: @Composable ((Color) -> Unit)? = null,
//    iconPosition: ButtonIconPosition = ButtonIconPosition.None
//) {
//    StatefulButton(
//        key = key,
//        text = text,
//        state = state,
//        radius = radius,
//        height = height,
//        enabled = enabled,
//        onClick = onClick,
//        modifier = modifier,
//        debounce = debounce,
//        background = background,
//        textColor = textColor,
//        textWeight = textWeight,
//        leadingIcon = leadingIcon,
//        type = ButtonType.Secondary,
//        iconPosition = iconPosition
//    )
//}
//
//@Composable
//fun TertiaryButton(
//    modifier: Modifier = Modifier,
//    text: String,
//    onClick: () -> Unit,
//    height: ButtonHeight,
//    key: Any? = null,
//    radius: Dp? = null,
//    enabled: Boolean = true,
//    background: Background.Solid? = null,
//    textColor: Color? = null,
//    debounce: Boolean = false,
//    textWeight: Boolean = false,
//    state: ButtonState = ButtonState.Default,
//    leadingIcon: @Composable ((Color) -> Unit)? = null,
//    iconPosition: ButtonIconPosition = ButtonIconPosition.None
//) {
//    StatefulButton(
//        key = key,
//        text = text,
//        state = state,
//        radius = radius,
//        height = height,
//        enabled = enabled,
//        onClick = onClick,
//        modifier = modifier,
//        debounce = debounce,
//        background = background,
//        textColor = textColor,
//        textWeight = textWeight,
//        leadingIcon = leadingIcon,
//        type = ButtonType.Tertiary,
//        iconPosition = iconPosition
//    )
//}
//
//@Composable
//fun StatefulButton(
//    modifier: Modifier = Modifier,
//    text: String,
//    type: ButtonType,
//    state: ButtonState,
//    onClick: () -> Unit,
//    height: ButtonHeight,
//    key: Any? = null,
//    radius: Dp? = null,
//    enabled: Boolean = true,
//    background: Background.Solid? = null,
//    textColor: Color? = null,
//    debounce: Boolean = false,
//    textWeight: Boolean = false,
//    leadingIcon: @Composable ((Color) -> Unit)? = null,
//    trailingIcon: @Composable ((Color) -> Unit)? = null,
//    iconPosition: ButtonIconPosition = ButtonIconPosition.None
//) {
//    key(key) {
//        val scale = if (debounce) {
//            modifier.clickableSingle(enabled, onClick = onClick, percentage = .95f)
//        } else {
//            modifier.scaleWhenPressed(onClick, enabled)
//        }
//        val spinnerResId = when(type) {
//            ButtonType.Super -> R.drawable.icon_spinner_dark
//            ButtonType.Primary -> R.drawable.icon_spinner_light
//            ButtonType.Secondary -> R.drawable.icon_spinner_dark
//            ButtonType.Tertiary -> if (isSystemInDarkTheme()) R.drawable.icon_spinner_dark else R.drawable.icon_spinner_light
//        }
//        ButtonImpl(
//            text = text,
//            modifier = scale,
//            textWeight = textWeight,
//            leadingIcon = leadingIcon,
//            trailingIcon = trailingIcon,
//            spinnerResId = spinnerResId,
//            buttonProperties = buttonProperties(
//                type = type,
//                state = state,
//                radius = radius,
//                height = height,
//                background = background,
//                textColor = textColor,
//                iconPosition = iconPosition
//            )
//        )
//    }
//}
//
//
//@Preview
//@Composable
//fun SmallIconButton(
//    modifier: Modifier = Modifier,
//    @DrawableRes iconId: Int = R.drawable.icon_arrow_left,
//    tint: Color = Color.Black,
//    enabled: Boolean = true,
//    onClick: () -> Unit = {}
//) {
//    Icon(
//        painter = painterResource(id = iconId),
//        contentDescription = null,
//        tint = tint,
//        modifier = Modifier
//            .size(24.dp)
//            .clickableSingle(enabled = enabled, onClick = onClick)
//            .then(modifier)
//    )
//}
//
//@Composable
//private fun ButtonImpl(
//    modifier: Modifier = Modifier,
//    text: String,
//    textWeight: Boolean,
//    buttonProperties: ButtonProperties,
//    spinnerResId: Int,
//    leadingIcon: @Composable ((Color) -> Unit)? = null,
//    trailingIcon: @Composable ((Color) -> Unit)? = null
//) = with(buttonProperties) {
//    val shape = RoundedCornerShape(radius)
////    val backgroundModifier = when (val background = background) {
////        is Background.Solid -> modifier
////            .background(color = background.color.copy(alpha = buttonProperties.alpha), shape = shape)
////        is Background.Gradient -> modifier
////            .background(brush = background.color, shape = shape, alpha = buttonProperties.alpha)
////    }
//
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = backgroundModifier
//            .heightIn(height, height)
//    ) {
//        if (state == ButtonState.Loading) {
//            LoadingSpinner(painterResId = spinnerResId)
//        } else {
//            Row(
//                modifier = Modifier.padding(horizontal = 16.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(iconPadding)
//            ) {
//                leadingIcon?.invoke(textColor)
//
//                val textModifier = if (textWeight) Modifier.weight(1f) else Modifier
//
//                Text(
//                    text = text,
//                    color = textColor,
//                    textAlign = TextAlign.Center,
//                    style = buttonProperties.textStyle,
//                    modifier = textModifier
//                        .alpha(buttonProperties.alpha),
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//                trailingIcon?.invoke(textColor)
//            }
//        }
//    }
//}
//
//enum class ButtonType {
//    Super, Primary, Secondary, Tertiary
//}
//
//enum class ButtonState {
//    Default, Loading, Disabled
//}
//
//enum class ButtonIconPosition {
//    None, Left, Center, Both
//}
//
//enum class ButtonHeight {
//    H64, H52, H48, H44, H40, H38, H32
//}
//
//@Immutable
//interface ButtonProperties: Properties {
//    val iconPadding: Dp
//    val state: ButtonState
//    val textStyle: TextStyle
//    val alpha: Float
//}
//
//@Composable
//private fun buttonProperties(
//    radius: Dp?,
//    type: ButtonType,
//    state: ButtonState,
//    height: ButtonHeight,
//    background: Background.Solid? = null,
//    textColor: Color? = null,
//    iconPosition: ButtonIconPosition,
//): ButtonProperties = with(AlgorithmTheme) {
//    val (defaultBackground, defaultTextColor) = with(colors) {
//        when (type) {
//            ButtonType.Super -> {
//                when (state) {
//                    ButtonState.Default -> Background.Gradient(gradient1) to label1OnPrimary
//                    ButtonState.Disabled -> Background.Gradient(gradient1) to label1OnPrimary
//                    ButtonState.Loading -> Background.Gradient(gradient1) to label1OnPrimary
//                }
//            }
//            ButtonType.Primary -> {
//                when (state) {
//                    ButtonState.Default -> Background.Solid(white) to black
//                    ButtonState.Disabled -> Background.Solid(white) to black
//                    ButtonState.Loading -> Background.Solid(white) to black
//                }
//            }
//            ButtonType.Secondary -> {
//                when (state) {
//                    ButtonState.Default -> Background.Solid(primary) to label1Opposite
//                    ButtonState.Disabled -> Background.Solid(primary) to label1Opposite
//                    ButtonState.Loading -> Background.Solid(primary) to label1Opposite
//                }
//            }
//            ButtonType.Tertiary -> {
//                when (state) {
//                    ButtonState.Default -> Background.Solid(surfaceBlueLight) to label1
//                    ButtonState.Disabled -> Background.Solid(surfaceBlueLight) to label1
//                    ButtonState.Loading -> Background.Solid(surfaceBlueLight) to label1
//                }
//            }
//        }
//    }
//
//    val buttonAlpha = if (state == ButtonState.Disabled) 0.4f else 1f
//
//    val iconPadding = when (iconPosition) {
//        ButtonIconPosition.Both -> 8.dp
//        ButtonIconPosition.Center -> 10.dp
//        else -> if (height == ButtonHeight.H64) 10.dp else 4.dp
//    }
//
//    val textStyle = with(typography) {
//        when (height) {
//            ButtonHeight.H32 -> captionBold
//            ButtonHeight.H64 -> if (type == ButtonType.Primary) headlineBold else bodyBold
//            ButtonHeight.H40, ButtonHeight.H38 -> footnoteBold
//            else -> bodyBold
//        }
//    }
//
//    val heightDp = when (height) {
//        ButtonHeight.H64 -> 64.dp
//        ButtonHeight.H52 -> 52.dp
//        ButtonHeight.H48 -> 48.dp
//        ButtonHeight.H44 -> 44.dp
//        ButtonHeight.H40 -> 40.dp
//        ButtonHeight.H38 -> 38.dp
//        ButtonHeight.H32 -> 32.dp
//    }
//
//    return remember(type, state, height, iconPosition, textColor, defaultBackground, buttonAlpha) {
//        object : ButtonProperties {
//            override val height: Dp = heightDp
//            override val iconPadding: Dp = iconPadding
//            override val textStyle: TextStyle = textStyle
//            override val alpha: Float = buttonAlpha
//            override val radius: Dp = radius ?: 16.dp
//            override val background = background ?: defaultBackground
//            override val textColor: Color = textColor ?: defaultTextColor
//            override val state: ButtonState = state
//        }
//    }
//}