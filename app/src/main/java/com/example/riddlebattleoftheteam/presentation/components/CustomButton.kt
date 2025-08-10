package com.example.riddlebattleoftheteam.presentation.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import com.example.riddlebattleoftheteam.ui.theme.DarkGreen
import com.example.riddlebattleoftheteam.ui.theme.Dimens

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Button(
        shape = RoundedCornerShape(Dimens.ButtonCornerRadius),
        modifier = modifier
            .height(Dimens.ButtonHeight)
            .widthIn(Dimens.ButtonWidth)
            .scale(if (isPressed) 0.90f else 1f),
        interactionSource = interactionSource,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = DarkGreen,
        ),
    ) {
        Text(
            color = color,
            fontSize = Dimens.SmallText,
            text = text,
        )
    }
}