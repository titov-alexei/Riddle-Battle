package com.example.riddlebattleoftheteam.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.riddlebattleoftheteam.ui.theme.DarkGreen
import com.example.riddlebattleoftheteam.ui.theme.Dimens


@Composable
fun CustomBox(
    modifier: Modifier = Modifier.padding(horizontal = Dimens.CustomBoxPadding),
    backgroundColor: Color = Color.White,
    cornerRadius: Dp = Dimens.ButtonCornerRadius,
    text: String = "",
    showBorder: Boolean = false,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clickable { onClick() }
            .background(
                color = backgroundColor.copy(alpha = 0.3f),
                shape = RoundedCornerShape(cornerRadius)
            )
            .then(
                if (showBorder) {
                    Modifier
                        .border(
                            width = 3.dp,
                            color = DarkGreen,
                            shape = RoundedCornerShape(cornerRadius))
                } else {
                    Modifier
                }
            )
            .padding(Dimens.SpacerPadding)
            .fillMaxWidth(),
            contentAlignment = Alignment.Center
    ) {
        Text(
            fontSize = Dimens.SmallText,
            text = text
        )
    }
}