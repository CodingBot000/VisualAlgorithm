package com.algorithm.presentation.ui.component

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.algorithm.presentation.R
import com.algorithm.presentation.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogBottomSheet(
    modifier: Modifier = Modifier,
    logHistoryString: String,
    closeSheet: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = closeSheet,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = CustomTheme.colors.bg,
        dragHandle = null
    ) {

        Column(modifier = modifier
            .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
            .navigationBarsPadding()
        ) {
            Text(
                text = stringResource(id = R.string.log_history),
                color = CustomTheme.colors.textColorPrimary,
                style = CustomTheme.typography.title2Bold
            )
            Spacer(modifier = modifier.size(4.dp))
            Text(
                modifier = Modifier
                    .height(200.dp)
                    .verticalScroll(rememberScrollState()),
                text = if (logHistoryString.isEmpty()) {
                    stringResource(id = R.string.log_history_empty_msg)
                } else {
                    logHistoryString
                },
                color = CustomTheme.colors.textColorPrimary,
                style = CustomTheme.typography.bodyRegular
            )
        }
    }
}